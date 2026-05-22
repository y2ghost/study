package study.ywork.users.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import study.ywork.users.domain.Car;
import study.ywork.users.domain.CarPark;
import study.ywork.users.domain.SPELExpression;

@SpringBootTest(classes = {SPELConfiguration.class})
public class SPELConfigurationTests {
    @Autowired
    private SPELExpression expression;

    @Test
    void testSPELValues() {
        Assertions.assertNotNull(expression.toString());
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("'Any string'");
        Assertions.assertEquals("Any string", expression.getValue());

        expression = expressionParser.parseExpression("'Any string'.length()");
        Assertions.assertEquals(10, expression.getValue());

        expression = expressionParser.parseExpression("new String('Any string').length()");
        Assertions.assertEquals(10, expression.getValue());

        expression = expressionParser.parseExpression("'Any string'.replace(\" \", \"\").length()");
        Assertions.assertEquals(9, expression.getValue());
    }

    @Test
    void testParseExpression() {
        Car car = new Car();
        car.setMake("dev");
        car.setModel("red");
        car.setYearOfProduction(2026);

        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("model");

        EvaluationContext context = new StandardEvaluationContext(car);
        Assertions.assertEquals("red", expression.getValue(context));

        expression = expressionParser.parseExpression("yearOfProduction > 2005");
        Assertions.assertEquals(true, expression.getValue(car, Boolean.class));
    }

    @Test
    void testSetValue() {
        Car car = new Car();
        car.setMake("dev");
        car.setModel("white");
        car.setYearOfProduction(2026);

        CarPark carPark = new CarPark();
        carPark.getCars().add(car);

        StandardEvaluationContext context = new StandardEvaluationContext(carPark);

        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("cars[0].model");
        expression.setValue(context, "red");
        Assertions.assertEquals("red", expression.getValue(context));
    }
}
