package study.ywork.upload.service;

import study.ywork.upload.service.dto.FileUploadDTO;
import study.ywork.upload.web.vm.FileUploadVM;

public interface SliceUploadStrategy {
    FileUploadDTO sliceUpload(FileUploadVM param);
}
