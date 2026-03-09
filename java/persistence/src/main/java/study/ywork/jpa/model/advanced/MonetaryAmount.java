package study.ywork.jpa.model.advanced;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public class MonetaryAmount implements Serializable {
    protected final BigDecimal value;
    protected final Currency currency;

    public MonetaryAmount(BigDecimal value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof final MonetaryAmount monetaryAmount)) return false;

        if (!value.equals(monetaryAmount.value)) return false;
        return currency.equals(monetaryAmount.currency);
    }

    public int hashCode() {
        int result;
        result = value.hashCode();
        result = 29 * result + currency.hashCode();
        return result;
    }

    public String toString() {
        return getValue() + " " + getCurrency();
    }

    public static MonetaryAmount fromString(String s) {
        String[] split = s.split(" ");
        return new MonetaryAmount(
                new BigDecimal(split[0]),
                Currency.getInstance(split[1])
        );
    }
}

