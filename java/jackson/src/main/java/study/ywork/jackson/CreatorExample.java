package study.ywork.jackson;

import java.beans.ConstructorProperties;
import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 使用@JsonCreator注解定义反序列化的构造函数或工厂方法
 * 该注解仅在反序列化期间使用，并且对于不可变对象特别有用
 */
public class CreatorExample {
    public static void main(String[] args) throws IOException {
        System.out.println("-- 序列化 --");
        Employee employee = new Employee();
        employee.setName("yy");
        employee.setDept("admin");
        String json = toJson(employee);
        System.out.println(json);

        System.out.println("-- 反序列化 --");
        Employee e = toEmployee(json);
        System.out.println(e);
    }

    private static Employee toEmployee(String jsonData) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(jsonData, Employee.class);
    }

    private static String toJson(Employee employee) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(employee);
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        private String dept;

        public Employee() {
        }

        /*
         * 多个注解存在时，优先使用构造函数的 可以在构造函数上注解属性，例子如下：
         * @JsonCreator
         * public Employee(@JsonProperty("name") String name, @JsonProperty("dept") String dept)
         */
        @ConstructorProperties({ "name", "dept" })
        public Employee(String name, String dept) {
            System.out.println("JsonCreator构造函数被调用1");
            this.name = name;
            this.dept = dept;
        }

        @JsonCreator
        public static Employee of(@JsonProperty("name") String name, @JsonProperty("dept") String dept) {
            System.out.println("JsonCreator构造函数被调用2");
            Employee employee = new Employee();
            employee.name = name;
            employee.dept = dept;
            return employee;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public String getName() {
            return name;
        }

        public String getDept() {
            return dept;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + ", dept=" + dept + "]";
        }
    }
}
