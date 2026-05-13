package study.ywork.jackson;

import java.time.ZonedDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * @JsonFormat注解用于序列化特殊的类型:Date, Collection, Enum
 */
public class FormatExample {
    public static void main(String[] args) throws IOException {
        EmployeeEx employeeEx = new EmployeeEx();
        employeeEx.setName("yy");
        employeeEx.setDateOfBirth(Date.from(ZonedDateTime.now().minusYears(30).toInstant()));
        employeeEx.setStartDate(Date.from(ZonedDateTime.now().minusYears(2).toInstant()));
        employeeEx.setDept(Dept.SALES);
        ArrayListEx<String> list = new ArrayListEx<>();
        list.add("111-111-111");
        list.add("222-222-222");
        employeeEx.setPhoneNumbers(list);
        employeeEx.setSalary(new BigIntegerEx("4000"));

        System.out.println("-- 序列化之前 --");
        System.out.println(employeeEx);

        System.out.println("-- 序列化之后 --");
        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(employeeEx);
        System.out.println(jsonString);

        System.out.println("-- 反序列化为Employee之后 --");
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        System.out.println(om.readValue(jsonString, Employee.class));

        EmployeeType employeeType = EmployeeType.FULL_TIME;
        System.out.println("-- employeeType 枚举类型序列化为JSON对象字符串 --");
        om = new ObjectMapper();
        om.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        jsonString = om.writeValueAsString(employeeType);
        System.out.println(jsonString);

        // 测试未知值的枚举反序列化时使用默认值
        jsonString = "\"test\"";
        System.out.println("-- 枚举默认值测试 --");
        System.out.println(jsonString);

        om = new ObjectMapper();
        om.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        EmployeeTypeEx typeEx = om.readValue(jsonString, EmployeeTypeEx.class);
        System.out.println("-- 反序列化之后 --");
        System.out.println(typeEx);
    }

    private enum Dept {
        ADMIN, IT, SALES
    }

    /*
     * 枚举类型支持序列化为JSON对象字符串，但不支持相反的操作
     * 
     * @JsonEnumDefaultValue指定默认枚举值
     */

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @SuppressWarnings("unused")
    private enum EmployeeType {
        FULL_TIME("Full Time"), PART_TIME("Part Time");

        private String displayName;

        EmployeeType() {
        }

        EmployeeType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum EmployeeTypeEx {
        FULL_TIME, PART_TIME, @JsonEnumDefaultValue
        CONTRACTOR;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private static class BigIntegerEx extends BigInteger {
        private static final long serialVersionUID = 1L;

        public BigIntegerEx() {
            super("");
        }

        public BigIntegerEx(String val) {
            super(val);
        }
    }

    @SuppressWarnings("unused")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private static class ArrayListEx<T> extends AbstractList<T> {
        private List<T> wrapperList = new ArrayList<>();

        @Override
        public boolean add(T t) {
            return wrapperList.add(t);
        }

        @Override
        public T get(int index) {
            return wrapperList.get(index);
        }

        @Override
        public int size() {
            return wrapperList.size();
        }

        public List<T> getWrapperList() {
            return wrapperList;
        }

        public void setWrapperList(List<T> wrapperList) {
            this.wrapperList = wrapperList;
        }

        @JsonIgnore
        @Override
        public boolean isEmpty() {
            return super.isEmpty();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof ArrayListEx)) {
                return false;
            }

            return wrapperList != null && wrapperList.equals(((ArrayListEx<?>) o).wrapperList);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(wrapperList);
        }
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Shanghai")
        private Date dateOfBirth;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
        private Date startDate;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private Dept dept;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Dept getDept() {
            return dept;
        }

        public void setDept(Dept dept) {
            this.dept = dept;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + ", dateOfBirth=" + dateOfBirth + ", startDate=" + startDate + ", dept="
                + dept + "]";
        }
    }

    @SuppressWarnings("unused")
    private static class EmployeeEx extends Employee {
        private ArrayListEx<String> phoneNumbers;
        private BigIntegerEx salary;

        public ArrayListEx<String> getPhoneNumbers() {
            return phoneNumbers;
        }

        public void setPhoneNumbers(ArrayListEx<String> phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }

        public BigIntegerEx getSalary() {
            return salary;
        }

        public void setSalary(BigIntegerEx salary) {
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "EmployeeEx [phoneNumbers=" + phoneNumbers + ", salary=" + salary + super.toString() + "]";
        }
    }
}
