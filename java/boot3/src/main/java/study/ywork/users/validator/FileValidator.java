package study.ywork.users.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import study.ywork.users.util.FileNameUtils;

import java.util.List;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    private List<String> extensions;
    private List<String> mimeTypes;
    private long maxSize;

    @Override
    public void initialize(ValidFile validFile) {
        extensions = List.of(validFile.extensions());
        mimeTypes = List.of(validFile.mimeTypes());
        maxSize = validFile.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        // 验证文件大小
        if (file.getSize() > maxSize) {
            return false;
        }

        // 验证文件扩展名
        String fileExtension = FileNameUtils.getExtension(file.getOriginalFilename());
        if (extensions.contains(fileExtension.toLowerCase())) {
            return true;
        }

        // 验证文件的互联网媒体类型
        String contentType = file.getContentType();
        return mimeTypes.contains(contentType);
    }
}
