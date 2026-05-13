package study.ywork.hibernate.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { ValidPassengerCount.Validator.class })
@Documented
public @interface ValidPassengerCount {

    String message() default "座位数少于2或是乘客为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidPassengerCount, Taxi> {
        @Override
        public void initialize(ValidPassengerCount constraintAnnotation) {
            // NOTHING HERE
        }

        @Override
        public boolean isValid(Taxi taxi, ConstraintValidatorContext context) {
            return taxi.getSeatCount() >= 2 && null != taxi.getPassengers();
        }
    }
}