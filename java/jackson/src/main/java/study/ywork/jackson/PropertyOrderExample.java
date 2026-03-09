package study.ywork.jackson;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

/* 
 * 指定序列化字段顺序
 */
public class PropertyOrderExample {
    public static void main(String[] args) throws IOException {
        Employee e = new Employee();
        e.setId("1");
        e.setName("yy");
        e.setSalary(3000);
        e.setPhoneNumber("11111111111");
        e.setEmail("jackson@example.com");
        e.setOtherDetails(Map.of("begin", "2020", "supervisor", "yy", "budget", "3000"));
        System.out.println("-- 开始序列化之前 --");
        System.out.println(e);

        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(e);
        System.out.println("-- 序列化之后 --");
        System.out.println(jsonString);
    }

    @SuppressWarnings("unused")
    @JsonPropertyOrder(value = { "name", "phoneNumber", "email", "salary", "id" }, alphabetic = true)
    private static class Employee {
        private String id;
        private String name;
        private Integer salary;
        private String phoneNumber;
        private String email;
        @JsonPropertyOrder(alphabetic = true)
        private Map<String, String> otherDetails;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getSalary() {
            return salary;
        }

        public void setSalary(Integer salary) {
            this.salary = salary;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Map<String, String> getOtherDetails() {
            return otherDetails;
        }

        public void setOtherDetails(Map<String, String> otherDetails) {
            this.otherDetails = otherDetails;
        }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + ", phoneNumber=" + phoneNumber
                + ", email=" + email + ", otherDetails=" + otherDetails + "]";
        }
    }
}
