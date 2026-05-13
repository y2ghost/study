package study.ywork.hibernate.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxiTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .addValueExtractor(new GearBoxValueExtractor())
            .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void taxiPassengersIsNull() {
        Taxi taxi = new Taxi();
        taxi.setSeatCount(4);
        Set<ConstraintViolation<Taxi>> constraintViolations = validator.validate(taxi);
        assertEquals(1, constraintViolations.size());
        assertEquals("座位数少于2或是乘客为空", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void taxiIsValid() {
        Taxi taxi = new Taxi();
        taxi.setSeatCount(4);
        List<Person> passengers = new ArrayList<>(1);
        Person person = new Person("test", "female");
        passengers.add(person);
        taxi.setPassengers(passengers);
        Set<ConstraintViolation<Taxi>> constraintViolations = validator.validate(taxi);
        assertEquals(0, constraintViolations.size());
    }
}
