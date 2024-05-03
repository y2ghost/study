package study.ywork.jackson;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * @JsonAlias注解反序列化时定义属性别名，以便能够映射正确的POJO对象属性字段
 */
public class AliasExample {
    public static void main(String[] args) throws IOException {
        ObjectMapper om = new ObjectMapper();
        System.out.println("-- 反序列化 --");
        String jsonData = "{\"name\":\"yy\",\"department\":\"IT\"}";
        Employee employee = om.readValue(jsonData, Employee.class);
        System.out.println(employee);

        jsonData = "{\"name\":\"yy\",\"employeeDept\":\"IT\"}";
        employee = om.readValue(jsonData, Employee.class);
        System.out.println(employee);

        System.out.println("-- 序列化 --");
        Employee e = Employee.of("yy", "admin");
        String s = om.writeValueAsString(e);
        System.out.println(s);
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        @JsonAlias({ "department", "employeeDept" })
        private String dept;

        public static Employee of(String name, String dept) {
            Employee e = new Employee();
            e.name = name;
            e.dept = dept;
            return e;
        }

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
