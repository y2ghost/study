package study.ywork.jackson;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 忽略序列化的字段方式：字段上定义、类上定义
 */
public class IgnoreExample {
    private static final ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Address address = new Address();
        address.setCity("yueyang");
        address.setStreet("one road");
        Employee employee = new Employee();
        employee.setName("test");
        employee.setDept("dev");
        employee.setJob("IT");
        employee.setAddress(address);

        String json = toJson(address);
        System.out.println(address);
        System.out.println(json);

        json = toJson(employee);
        System.out.println(employee);
        System.out.println(json);

        employee = toObject("{\"job\":\"IT\",\"name\":\"test\",\"dept\":\"dev\"}", Employee.class);
        System.out.println(employee);
    }

    private static <T> T toObject(String json, Class<T> valueType) throws IOException {
        return om.readValue(json, valueType);
    }

    private static String toJson(Object obj) throws IOException {
        return om.writeValueAsString(obj);
    }

    /*
     * 忽略整个类的序列化和序列化，只有它作为属性时有效 如果自身进行序列化，则没有问题
     */
    @JsonIgnoreType
    @SuppressWarnings("unused")
    private static class Address {
        private String street;
        private String city;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "Address [street=" + street + ", city=" + city + "]";
        }
    }

    // 类上注解忽略的属性
    @SuppressWarnings("unused")
    @JsonIgnoreProperties(value = { "dept" }, ignoreUnknown = true)
    private static class Employee {
        private String name;
        private String dept;
        // 方法上注解忽略的属性
        @JsonIgnore
        private String job;
        private Address address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDept() {
            return dept;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + ", dept=" + dept + ", job=" + job + ", address=" + address + "]";
        }
    }
}
