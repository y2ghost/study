package study.ywork.users.domain;

import org.springframework.beans.factory.annotation.Value;

public class SPELExpression {
    /**
     * 算术运算符操作
     */
    @Value("#{19 + 1}")
    private double add;

    @Value("#{'String1 ' + 'string2'}")
    private String addString;

    @Value("#{20 - 1}")
    private double subtract;

    @Value("#{10 * 2}")
    private double multiply;

    @Value("#{36 / 2}")
    private double divide;

    @Value("#{36 div 2}")
    private double divideAlphabetic;

    @Value("#{37 % 10}")
    private double modulo;

    @Value("#{37 mod 10}")
    private double moduloAlphabetic;

    @Value("#{2 ^ 9}")
    private double powerOf;

    @Value("#{(2 + 2) * 2 + 9}")
    private double brackets;

    /**
     * 关系和逻辑运算符
     */
    @Value("#{1 == 1}")
    private boolean equal;

    @Value("#{1 eq 1}")
    private boolean equalAlphabetic;

    @Value("#{1 != 1}")
    private boolean notEqual;

    @Value("#{1 ne 1}")
    private boolean notEqualAlphabetic;

    @Value("#{1 < 1}")
    private boolean lessThan;

    @Value("#{1 lt 1}")
    private boolean lessThanAlphabetic;

    @Value("#{1 <= 1}")
    private boolean lessThanOrEqual;

    @Value("#{1 le 1}")
    private boolean lessThanOrEqualAlphabetic;

    @Value("#{1 > 1}")
    private boolean greaterThan;

    @Value("#{1 gt 1}")
    private boolean greaterThanAlphabetic;

    @Value("#{1 >= 1}")
    private boolean greaterThanOrEqual;

    @Value("#{1 ge 1}")
    private boolean greaterThanOrEqualAlphabetic;

    @Value("#{250 > 200 && 200 < 4000}")
    private boolean and;

    @Value("#{250 > 200 and 200 < 4000}")
    private boolean andAlphabetic;

    @Value("#{400 > 300 || 150 < 100}")
    private boolean or;

    @Value("#{400 > 300 or 150 < 100}")
    private boolean orAlphabetic;

    @Value("#{!true}")
    private boolean not;

    @Value("#{not true}")
    private boolean notAlphabetic;

    @Value("#{2 > 1 ? 'a' : 'b'}")
    private String ternary;

    /**
     * 正则表达式
     */
    @Value("#{'100' matches '\\d+' }")
    private boolean validNumericStringResult;

    @Value("#{'100fghdjf' matches '\\d+' }")
    private boolean invalidNumericStringResult;

    @Value("#{'valid alphabetic string' matches '[a-zA-Z\\s]+' }")
    private boolean validAlphabeticStringResult;

    @Value("#{'invalid alphabetic string #$1' matches '[a-zA-Z\\s]+' }")
    private boolean invalidAlphabeticStringResult;

    /**
     * Bean对象操作
     */
    @Value("#{carPark.carsByDriver['Driver1']}")
    private Car driver1Car;
    @Value("#{carPark.carsByDriver['Driver2']}")
    private Car driver2Car;
    @Value("#{carPark.cars[0]}")
    private Car firstCarInPark;
    @Value("#{carPark.cars.size()}")
    private Integer numberOfCarsInPark;

    @Override
    public String toString() {
        return "SPELConfiguration{" +
                "add=" + add +
                ", addString='" + addString + '\'' +
                ", subtract=" + subtract +
                ", multiply=" + multiply +
                ", divide=" + divide +
                ", divideAlphabetic=" + divideAlphabetic +
                ", modulo=" + modulo +
                ", moduloAlphabetic=" + moduloAlphabetic +
                ", powerOf=" + powerOf +
                ", brackets=" + brackets +
                ", equal=" + equal +
                ", equalAlphabetic=" + equalAlphabetic +
                ", notEqual=" + notEqual +
                ", notEqualAlphabetic=" + notEqualAlphabetic +
                ", lessThan=" + lessThan +
                ", lessThanAlphabetic=" + lessThanAlphabetic +
                ", lessThanOrEqual=" + lessThanOrEqual +
                ", lessThanOrEqualAlphabetic=" + lessThanOrEqualAlphabetic +
                ", greaterThan=" + greaterThan +
                ", greaterThanAlphabetic=" + greaterThanAlphabetic +
                ", greaterThanOrEqual=" + greaterThanOrEqual +
                ", greaterThanOrEqualAlphabetic=" + greaterThanOrEqualAlphabetic +
                ", and=" + and +
                ", andAlphabetic=" + andAlphabetic +
                ", or=" + or +
                ", orAlphabetic=" + orAlphabetic +
                ", not=" + not +
                ", notAlphabetic=" + notAlphabetic +
                ", ternary='" + ternary + '\'' +
                ", validNumericStringResult=" + validNumericStringResult +
                ", invalidNumericStringResult=" + invalidNumericStringResult +
                ", validAlphabeticStringResult=" + validAlphabeticStringResult +
                ", invalidAlphabeticStringResult=" + invalidAlphabeticStringResult +
                ", driver1Car=" + driver1Car +
                ", driver2Car=" + driver2Car +
                ", firstCarInPark=" + firstCarInPark +
                ", numberOfCarsInPark=" + numberOfCarsInPark +
                '}';
    }
}
