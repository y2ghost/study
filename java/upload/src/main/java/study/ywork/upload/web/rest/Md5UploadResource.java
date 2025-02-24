package study.ywork.upload.web.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.ywork.upload.exception.BusinessException;
import study.ywork.upload.service.Md5UploadService;
import study.ywork.upload.service.dto.FileUploadDTO;
import study.ywork.upload.service.dto.ResultDTO;
import study.ywork.upload.util.FileUtils;
import study.ywork.upload.web.vm.FileDownloadVM;
import study.ywork.upload.web.vm.FileUploadVM;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/md5-file")
public class Md5UploadResource {
    private final Logger log = LoggerFactory.getLogger(Md5UploadResource.class);
    private final Md5UploadService uploadService;

    public Md5UploadResource(Md5UploadService uploadService) {
        this.uploadService = uploadService;
    }


    @PostMapping("/upload")
    public ResultDTO<FileUploadDTO> upload(FileUploadVM uploadVM) throws IOException {
        if (null != uploadVM.getFile()) {
            FileUploadDTO uploadDTO = null;
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("upload");

            if (uploadVM.getChunk() != null && uploadVM.getChunks() > 0) {
                uploadDTO = uploadService.sliceUpload(uploadVM);
            } else {
                uploadDTO = uploadService.upload(uploadVM);
            }

            stopWatch.stop();
            log.info("upload finish {}", stopWatch.prettyPrint());
            return ResultDTO.ok(uploadDTO);
        }

        throw new BusinessException("上传失败");
    }

    @PostMapping("/checkFileMd5")
    public ResultDTO<FileUploadDTO> checkFileMd5(String md5) throws IOException {
        FileUploadVM param = new FileUploadVM();
        param.setMd5(md5);
        FileUploadDTO fileUploadDTO = uploadService.checkFileMd5(param);
        return ResultDTO.ok(fileUploadDTO);
    }

    @PostMapping("/download")
    public void download(FileDownloadVM requestDTO, HttpServletRequest request, HttpServletResponse response) {
        try {
            FileUtils.downloadFile(requestDTO.getName(), requestDTO.getPath(), request, response);
        } catch (FileNotFoundException e) {
            log.error("download error:" + e.getMessage(), e);
            throw new BusinessException("文件下载失败");
        }
    }
}
