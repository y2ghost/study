package study.ywork.upload.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import study.ywork.upload.domain.FileChunk;
import study.ywork.upload.domain.FileModel;
import study.ywork.upload.service.Sha256UploadService;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sha256-file")
public class Sha256UploadResource {
    private final Logger log = LoggerFactory.getLogger(Sha256UploadResource.class);
    private final String filePath;
    private final Sha256UploadService uploadService;

    public Sha256UploadResource(@Value("${upload.file.slice.dir}") String filePath, Sha256UploadService uploadService) {
        this.filePath = filePath;
        this.uploadService = uploadService;
    }

    /**
     * 上传完整的文件
     *
     * @param file 文件
     * @return true | false
     */
    @PostMapping("/uploadSingle")
    public Boolean upload(@RequestParam("file") MultipartFile file) {
        Path singleFilePath = Paths.get(filePath, file.getOriginalFilename());
        File dest = singleFilePath.toFile();

        try {
            file.transferTo(dest);
            return true;
        } catch (IOException e) {
            log.error(e.toString(), e);
        }

        return false;
    }

    /**
     * 上传文件前置任务
     * <p>
     * 通过文件大小以及内容Hash判断是否已经存在该文件
     * <p>
     * 如果不存在，返回已经上传的切片
     *
     * @param fileSize    文件大小
     * @param contentHash 文件内容的全量 Hash
     * @return {uploaded: false, uploadedChunkList: [FileChunk, FileChunk]}
     */
    @PostMapping("/prepare")
    public Object prepare(@RequestParam("fileSize") Long fileSize, @RequestParam("contentHash") String contentHash) {
        Boolean uploaded = uploadService.verifyContentHash(fileSize, contentHash);
        List<FileChunk> fileChunkList = new ArrayList<>();

        if (uploaded.equals(Boolean.FALSE)) {
            fileChunkList = uploadService.getChunkListByContentHash(contentHash);
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("uploaded", uploaded);
        returnMap.put("uploadedChunkList", fileChunkList);
        return returnMap;
    }

    /**
     * 上传切片文件
     *
     * @param chunk     切片文件
     * @param fileChunk 切片文件的源数据
     * @return true | false
     */
    @PostMapping("/uploadChunk")
    public Boolean upload(@RequestParam("chunk") MultipartFile chunk, FileChunk fileChunk) {
        Path fullPath = Paths.get(filePath, fileChunk.getFileName());

        // 模块写入对应的位置
        try (RandomAccessFile rf = new RandomAccessFile(fullPath.toString(), "rw")) {
            rf.seek(fileChunk.getStart());
            rf.write(chunk.getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

        // chunk 记录到数据库
        fileChunk.setFullPath(fullPath.toString());
        uploadService.addChunk(fileChunk);

        // 文件全部上传完成
        Integer chunkSize = uploadService.getChunkNumByContentHash(fileChunk.getFileHash());
        if (chunkSize.equals(fileChunk.getChunkNum())) {
            // 删除 chunk 记录
            uploadService.removeChunkRecord(fileChunk.getFileHash());
            // 增加 file 记录
            FileModel fileModel = new FileModel(fileChunk.getFileName(), fileChunk.getFullPath(),
                    fileChunk.getFileHash(), fileChunk.getTotal(), "SUCCESS");
            uploadService.addFile(fileModel);
        }

        return true;
    }
}
