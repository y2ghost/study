package study.ywork.jpa.converter;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;
import study.ywork.jpa.model.advanced.MonetaryAmount;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Properties;

public class MonetaryAmountUserType implements CompositeUserType, DynamicParameterizedType {
    protected Currency convertTo;

    @Override
    public void setParameterValues(Properties parameters) {

        /**
         * 获取特定属性示例
         * <code>
         * ParameterType parameterType = (ParameterType) parameters.get(PARAMETER_TYPE);
         * String[] columns = parameterType.getColumns();
         * String table = parameterType.getTable();
         * Annotation[] annotations = parameterType.getAnnotationsMethod();
         </code>
         */
        String convertToParameter = parameters.getProperty("convertTo");
        this.convertTo = Currency.getInstance(convertToParameter != null ? convertToParameter : "USD");
    }

    @Override
    public Class returnedClass() {
        return MonetaryAmount.class;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value, SharedSessionContractImplementor session) throws HibernateException {
        return value.toString();
    }

    @Override
    public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return MonetaryAmount.fromString((String) cached);
    }

    @Override
    public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public Object deepCopy(Object value) {
        return value;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return x == y || !(x == null || y == null) && x.equals(y);
    }

    @Override
    public int hashCode(Object x) {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        BigDecimal amount = rs.getBigDecimal(names[0]);
        if (rs.wasNull()) {
            return null;
        }

        Currency currency = Currency.getInstance(rs.getString(names[1]));
        return new MonetaryAmount(amount, currency);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, StandardBasicTypes.BIG_DECIMAL.sqlType());
            st.setNull(index + 1, StandardBasicTypes.CURRENCY.sqlType());
        } else {
            MonetaryAmount amount = (MonetaryAmount) value;
            MonetaryAmount dbAmount = convert(amount, convertTo);
            st.setBigDecimal(index, dbAmount.getValue());
            st.setString(index + 1, convertTo.getCurrencyCode());
        }
    }

    protected MonetaryAmount convert(MonetaryAmount amount, Currency toCurrency) {
        return new MonetaryAmount(amount.getValue().multiply(new BigDecimal(2)), toCurrency);
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{"value", "currency"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{
                StandardBasicTypes.BIG_DECIMAL,
                StandardBasicTypes.CURRENCY
        };
    }

    @Override
    public Object getPropertyValue(Object component,
                                   int property) {
        MonetaryAmount monetaryAmount = (MonetaryAmount) component;
        if (property == 0) {
            return monetaryAmount.getValue();
        } else {
            return monetaryAmount.getCurrency();
        }
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) {
        throw new UnsupportedOperationException("MonetaryAmount is immutable");
    }
}
