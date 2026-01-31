package study.ywork.hibernate.validator;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .addValueExtractor(new GearBoxValueExtractor())
            .buildValidatorFactory();
        // 实例线程安全，可以复用
        validator = factory.getValidator();
    }

    @Test
    void manufacturerIsNull() {
        Car car = new Car(null, "湘F1234C", 4);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(1, constraintViolations.size());
        assertEquals("不能为null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void licensePlateTooShort() {
        Car car = new Car("呆子汽车", "F", 4);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(1, constraintViolations.size());
        assertEquals("个数必须在2和14之间", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void seatCountTooLow() {
        Car car = new Car("呆子汽车", "湘F1234C", 1);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(1, constraintViolations.size());
        assertEquals("不能少于2", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void carIsValid() {
        Car car = new Car("呆子汽车", "湘F1234C", 4);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void validateMinTorque() {
        Car car = new Car("呆子汽车", "湘F1234C", 4);
        car.setGearBox(new GearBox<>(new Gear.AcmeGear()));
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<Car> constraintViolation = constraintViolations.iterator().next();
        assertEquals("不能少于100", constraintViolation.getMessage());
        assertEquals("gearBox", constraintViolation.getPropertyPath().toString());
    }

    @Test
    void testCheckCaseConstraint() {
        // 牌照小写，校验报错
        Car car = new Car("呆子汽车", "湘f1234c", 4);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(1, constraintViolations.size());
        assertEquals("Case mode must be UPPER.", constraintViolations.iterator().next().getMessage());

        // 牌照大写，校验成功
        car = new Car("呆子汽车", "湘F1234C", 4);
        constraintViolations = validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }
}
