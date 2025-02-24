package study.ywork.jackson;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * @JsonInclude注解用于指定那些属性可以进行序列化
 */
public class PropertyIncludeExample {
    public static void main(String[] args) throws IOException {
        Employee employee = Employee.of("yy", null, new AtomicReference<>());
        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(employee);
        System.out.println(jsonString);
    }

    @SuppressWarnings("unused")
    // 可以根据需求自行选择include的配置
    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private static class Employee {
        private String name;
        private String dept;
        private AtomicReference<String> address;

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

        public AtomicReference<String> getAddress() {
            return address;
        }

        public void setAddress(AtomicReference<String> address) {
            this.address = address;
        }

        public static Employee of(String name, String dept, AtomicReference<String> address) {
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
