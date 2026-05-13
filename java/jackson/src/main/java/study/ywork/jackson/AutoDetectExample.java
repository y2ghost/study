package study.ywork.jackson;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 默认情况下jackson使用公共的Setter, Getter方法进行序列化和反序列化
 * 如果不满足这样的情况，则可以通过@JsonAutoDetect注解指定如何访问属性的方法
 */
public class AutoDetectExample {
    public static void main(String[] args) throws IOException {
        Employee employee = Employee.of("yy", "Admin", "hunan yueyang");
        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(employee);
        System.out.println(jsonString);
        Employee e = om.readValue(jsonString, Employee.class);
        System.out.println(e);
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Employee {
        private String name;
        private String dept;
        private String address;

        public static Employee of(String name, String dept, String address) {
            Employee e = new Employee();
            e.name = name;
            e.dept = dept;
            e.address = address;
            return e;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + ", dept=" + dept + ", address=" + address + "]";
        }
    }
}
