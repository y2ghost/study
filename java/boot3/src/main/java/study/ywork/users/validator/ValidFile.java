package study.ywork.users.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用示例
 *
 * @ValidFile(extensions = {"jpg", "png"}, mimeTypes = {"image/jpeg", "image/png"}, maxSize = 2097152)
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface ValidFile {
    String message() default "验证文件出错";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] extensions() default {};

    String[] mimeTypes() default {};

    long maxSize() default 1048576;
}
