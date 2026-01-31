package study.ywork.hibernate.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import javax.validation.ConstraintDeclarationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class VehicleCarTest {
    private static ExecutableValidator executableValidator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        executableValidator = factory.getValidator().forExecutables();
    }

    @Test
    void illegalParameterConstraints() throws Exception {
        VehicleCar object = new VehicleCar();
        Method method = VehicleCar.class.getMethod("drive", int.class);
        Object[] parameterValues = {};
        assertThrows(ConstraintDeclarationException.class, () -> {
            executableValidator.validateParameters(object, method, parameterValues);
        });
    }
}
