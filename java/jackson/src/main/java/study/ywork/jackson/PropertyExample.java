package study.ywork.jackson;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 自定义序列化属性名称例子
 */
public class PropertyExample {
    private static final ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Employee employee = new Employee();
        employee.setName("test");
        employee.setDept("dev");

        String json = toJson(employee);
        System.out.println(json);

        employee = toEmployee(json);
        System.out.println(employee);
    }

    private static Employee toEmployee(String json) throws IOException {
        return om.readValue(json, Employee.class);
    }

    private static String toJson(Employee employee) throws IOException {
        return om.writeValueAsString(employee);
    }

    @SuppressWarnings("unused")
    private static class Employee {
        @JsonProperty("employee-name")
        private String name;
        @JsonProperty("employee-dept")
        private String dept;

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

        @Override
        public String toString() {
            return "Employee [name=" + name + ", dept=" + dept + "]";
        }
    }
}
