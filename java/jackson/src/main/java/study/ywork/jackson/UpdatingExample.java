package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 使用ObjectReader readerForUpdating(Object valueToUpdate)的方法得到ObjectReader对象
 * 然后使用public <T> T readValue(String jsonInput) throws IOException方法更新已有对象的字段
 * 使用JSON字符串的字段更新已有对象，未在JSON上的字段，对象上保留值不变
 * 对数组无效，因为数组一旦分配后长度不可变，所以不支持
 */
public class UpdatingExample {
    private static final String ADDRESS = "address";

    public static void main(String[] args) throws IOException {
        updatePojo();
        System.out.println();
        updateMap();
        System.out.println();
        updateCollection();
    }

    private static void showExistObjectHash(String json, Object existObj, Object insideObj) {
        System.out.println("输入的JSON字符串: " + json);
        System.out.println("已有对象: " + existObj);
        System.out.println("已有对象哈希: " + System.identityHashCode(existObj));
        System.out.println("已有对象的内部对象哈希: " + System.identityHashCode(insideObj));

    }

    private static void showUpdateObjectHash(Object updateObj, Object insideObj) {
        System.out.println("更新对象: " + updateObj);
        System.out.println("更新对象哈希: " + System.identityHashCode(updateObj));
        System.out.println("更新对象的内部对象哈希: " + System.identityHashCode(insideObj));

    }

    private static void updatePojo() throws IOException {
        String inputJson = "{\"name\":\"yy1\",\"salary\":3000,"
            + "\"address\":{\"street\":\"one street\",\"city\":\"yue yang\"}}";
        Employee existingEmployee = Employee.of("yy2", "Dev", 1000, "222-222-222", Address.of("hunan", "shiqu", "33"));
        showExistObjectHash(inputJson, existingEmployee, existingEmployee.getAddress());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForUpdating(existingEmployee);
        Employee updatedEmployee = objectReader.readValue(inputJson);
        showUpdateObjectHash(updatedEmployee, updatedEmployee.getAddress());
    }

    public static void updateMap() throws IOException {
        String inputJson = "{\"name\":\"yy1\",\"salary\":3000,"
            + "\"address\":{\"street\":\"one street\",\"city\":\"yue yang\"}}";
        Map<String, Object> existingMap = new HashMap<>(Map.of("name", "yy2", "dept", "Dev", "salary", 1000, "phone",
            "222-222-222", ADDRESS, new HashMap<>(Map.of("street", "hunan", "city", "shiqu", "zipCode", "33"))));
        showExistObjectHash(inputJson, existingMap, existingMap.get(ADDRESS));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForUpdating(existingMap);
        Map<String, Object> updatedMap = objectReader.readValue(inputJson);
        showUpdateObjectHash(updatedMap, updatedMap.get(ADDRESS));
    }

    public static void updateCollection() throws IOException {
        String inputJson = "[\"apple\", \"banana\" ]";
        List<String> existingList = new ArrayList<>(List.of("avocado", "mango", "cherries"));
        showExistObjectHash(inputJson, existingList, null);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForUpdating(existingList);
        List<String> updatedList = objectReader.readValue(inputJson);
        showUpdateObjectHash(updatedList, null);
    }

    @SuppressWarnings("unused")
    private static class Address {
        private String street;
        private String city;
        private String zipCode;

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

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public static Address of(String street, String city, String zipCode) {
            Address address = new Address();
            address.setStreet(street);
            address.setCity(city);
            address.setZipCode(zipCode);
            return address;
        }

        @Override
        public String toString() {
            return "Address{" + "street='" + street + '\'' + ", city='" + city + '\'' + ", zipCode=" + zipCode + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        private String dept;
        private Integer salary;
        private String phone;
        // @JsonMerge用于表明深度合并更新，如果没有则是浅更新，整个address被替换
        @JsonMerge
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

        public Integer getSalary() {
            return salary;
        }

        public void setSalary(Integer salary) {
            this.salary = salary;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public static Employee of(String name, String dept, Integer salary, String phone, Address address) {
            Employee e = new Employee();
            e.name = name;
            e.dept = dept;
            e.salary = salary;
            e.phone = phone;
            e.address = address;
            return e;
        }

        @Override
        public String toString() {
            return "Employee{" + "name='" + name + '\'' + ", dept='" + dept + '\'' + ", salary=" + salary + ", phone='"
                + phone + '\'' + ", address=" + address + '}';
        }
    }
}
