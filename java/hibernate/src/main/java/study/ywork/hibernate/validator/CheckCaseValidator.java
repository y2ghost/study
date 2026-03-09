package study.ywork.hibernate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// 自定义校验约束类
public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {
    private CaseMode caseMode;

    @Override
    public void initialize(CheckCase constraintAnnotation) {
        this.caseMode = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintContext) {
        if (null == str) {
            return true;
        }

        if (caseMode == CaseMode.UPPER) {
            return str.equals(str.toUpperCase());
        } else {
            return str.equals(str.toLowerCase());
        }
    }
}