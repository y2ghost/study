package study.ywork.jackson;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Setter, Getter方法上定义序列化字段名称
 */
public class GetterSetterExample {
    private static final ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Employee employee = new Employee();
        employee.setName("test");
        employee.setDept("dev");
        employee.setTest(null);

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
        private String name;
        private String dept;
        private String test;

        public String getTest() {
            return test;
        }

        @JsonSetter(nulls = Nulls.AS_EMPTY)
        public void setTest(String test) {
            this.test = test;
        }

        @JsonGetter("employee-name")
        public String getName() {
            return name;
        }

        @JsonSetter("employee-name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonGetter("employee-dept")
        public String getDept() {
            return dept;
        }

        @JsonSetter("employee-dept")
        public void setDept(String dept) {
            this.dept = dept;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + ", dept=" + dept + ", test=" + test + "]";
        }
    }
}
