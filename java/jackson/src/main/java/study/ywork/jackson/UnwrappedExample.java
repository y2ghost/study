package study.ywork.jackson;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 使用@JsonUnwrapped将属性序列化/反序列化为扁平化的数据结构
 */
public class UnwrappedExample {
    public static void main(String[] args) throws IOException {
        Department dept = new Department();
        dept.setDeptName("Admin");
        dept.setLocation("NY");
        Employee employee = new Employee();
        employee.setName("yy");
        employee.setDept(dept);

        System.out.println("-- 序列化之前 --");
        System.out.println(employee);

        System.out.println("-- 序列化之后 --");
        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(employee);
        System.out.println(jsonString);

        System.out.println("-- 反序列化之后 --");
        Employee employee2 = om.readValue(jsonString, Employee.class);
        System.out.println(employee2);
    }

    @SuppressWarnings("unused")
    private static class Department {
        private String deptName;
        private String location;

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return "Department [deptName=" + deptName + ", location=" + location + "]";
        }
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        /*
         * 可以自由指定前缀和后缀，它们默认为空 用法例子
         * 
         * @JsonUnwrapped(prefix = "dept-")
         * @JsonUnwrapped
         */
        @JsonUnwrapped(suffix = "-dept")
        private Department dept;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Department getDept() {
            return dept;
        }

        public void setDept(Department dept) {
            this.dept = dept;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + ", dept=" + dept + "]";
        }
    }
}
