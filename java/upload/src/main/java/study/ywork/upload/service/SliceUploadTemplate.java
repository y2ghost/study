package study.ywork.upload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.ywork.upload.constant.FileConstants;
import study.ywork.upload.service.dto.FileUploadDTO;
import study.ywork.upload.util.FileMD5Utils;
import study.ywork.upload.util.FileUtils;
import study.ywork.upload.web.vm.FileUploadVM;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class SliceUploadTemplate implements SliceUploadStrategy {
    protected final Logger log;
    protected final String sliceDir;
    protected final RedisService redisService;

    protected SliceUploadTemplate(String sliceDir, RedisService redisService) {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.sliceDir = sliceDir;
        this.redisService = redisService;
    }

    public abstract boolean upload(FileUploadVM param);

    protected File createTmpFile(FileUploadVM param) {
        String fileName = param.getFile().getOriginalFilename();
        String uploadDirPath = FileUtils.buildPath(sliceDir, param);
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath, tempFileName);

        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        return tmpFile;
    }

    @Override
    public FileUploadDTO sliceUpload(FileUploadVM param) {
        boolean isOk = this.upload(param);
        if (isOk) {
            File tmpFile = this.createTmpFile(param);
            return this.saveAndFileUploadDTO(param.getFile().getOriginalFilename(), tmpFile);
        }

        String md5 = FileMD5Utils.getFileMD5(param.getFile());
        Map<Integer, String> map = new HashMap<>();
        map.put(param.getChunk(), md5);
        FileUploadDTO uploadDTO = new FileUploadDTO();
        uploadDTO.setChunkMd5Info(map);
        return uploadDTO;
    }

    /**
     * 检查并修改文件上传进度
     */
    public boolean checkAndSetUploadProgress(FileUploadVM param, String uploadDirPath) {
        String fileName = param.getFile().getOriginalFilename();
        File confFile = new File(uploadDirPath, fileName + ".conf");
        byte isComplete = 0;

        try (RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw")) {
            // 把该分段标记为 true 表示完成
            System.out.println("set part " + param.getChunk() + " complete");
            // 创建conf文件文件长度为总分片数
            // 每上传一个分块即向conf文件中写入一个127
            // 那么没上传的位置就是默认0,已上传的就是Byte.MAX_VALUE 127
            accessConfFile.setLength(param.getChunks());
            accessConfFile.seek(param.getChunk());
            accessConfFile.write(Byte.MAX_VALUE);

            // completeList 检查是否全部完成,如果数组里是否全部都是127(全部分片都成功上传)
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            isComplete = Byte.MAX_VALUE;
            for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
                // 与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
                isComplete = (byte) (isComplete & completeList[i]);
                log.info("check part {} complete?: {}", i, completeList[i]);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return setUploadProgress2Redis(param, uploadDirPath, fileName, confFile, isComplete);
    }

    private boolean setUploadProgress2Redis(FileUploadVM param, String uploadDirPath, String fileName, File confFile,
                                            byte isComplete) {
        boolean result = false;
        if (isComplete == Byte.MAX_VALUE) {
            redisService.hashSet(FileConstants.FILE_UPLOAD_STATUS, param.getMd5(), "true");
            redisService.del(FileConstants.FILE_MD5_KEY + param.getMd5());

            try {
                Files.delete(confFile.toPath());
                result = true;
            } catch (IOException e) {
                log.error("delete conf file error: {}", confFile.getPath());
            }

            return result;
        }

        if (!redisService.hasHashKey(FileConstants.FILE_UPLOAD_STATUS, param.getMd5())) {
            redisService.hashSet(FileConstants.FILE_UPLOAD_STATUS, param.getMd5(), "false");
            Path path = Paths.get(uploadDirPath, fileName + ".conf");
            redisService.set(FileConstants.FILE_MD5_KEY + param.getMd5(), path.toString());
        }

        return false;
    }

    public FileUploadDTO saveAndFileUploadDTO(String fileName, File tmpFile) {
        FileUploadDTO uploadDTO = null;
        try {
            uploadDTO = renameFile(tmpFile, fileName);
            if (uploadDTO.isUploadComplete()) {
                log.info("upload complete: {} name {}", uploadDTO.isUploadComplete(), fileName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return uploadDTO;
    }

    private FileUploadDTO renameFile(File toBeRenamed, String toFileNewName) {
        // 检查要重命名的文件是否存在，是否是文件
        FileUploadDTO uploadDTO = new FileUploadDTO();
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            log.info("File does not exist: {}", toBeRenamed.getName());
            uploadDTO.setUploadComplete(false);
            return uploadDTO;
        }

        String ext = FileUtils.getExtension(toFileNewName);
        Path filePath = Paths.get(toBeRenamed.getParent(), toFileNewName);
        File newFile = filePath.toFile();
        // 修改文件名
        boolean uploadFlag = toBeRenamed.renameTo(newFile);
        uploadDTO.setMtime(Instant.now().getEpochSecond());
        uploadDTO.setUploadComplete(uploadFlag);
        uploadDTO.setPath(filePath.toString());
        uploadDTO.setSize(newFile.length());
        uploadDTO.setFileExt(ext);
        uploadDTO.setFileId(toFileNewName);
        return uploadDTO;
    }
}
