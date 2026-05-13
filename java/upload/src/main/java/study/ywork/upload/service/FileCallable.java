package study.ywork.upload.service;

import study.ywork.upload.service.dto.FileUploadDTO;
import study.ywork.upload.web.vm.FileUploadVM;

import java.util.concurrent.Callable;

public class FileCallable implements Callable<FileUploadDTO> {
    private SliceUploadStrategy uploadStrategy;
    private FileUploadVM param;

    public FileCallable(SliceUploadStrategy uploadStrategy, FileUploadVM param) {

        this.uploadStrategy = uploadStrategy;
        this.param = param;
    }

    @Override
    public FileUploadDTO call() {
        return uploadStrategy.sliceUpload(param);
    }
}
