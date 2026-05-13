package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.io.IOException;

/*
 * 使用@JsonFilter注解过滤掉指定的属性
 */
public class FilterExample {
    public static void main(String[] args) throws IOException {
        employeeFilter();
        System.out.println();
        personFilter();
        System.out.println();
    }

    public static void employeeFilter() throws IOException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("empFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name", "phone"));

        ObjectMapper om = new ObjectMapper();
        om.setFilterProvider(filterProvider);

        Employee e = Employee.of("yy", "Admin", 3000, "111-111-111");
        System.out.println(e);
        System.out.println("-- 序列化 --");
        String s = om.writeValueAsString(e);
        System.out.println(s);
    }

    public static void personFilter() throws IOException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("PersonAddressFilter", SimpleBeanPropertyFilter.filterOutAllExcept("zipCode"));

        ObjectMapper om = new ObjectMapper();
        om.setFilterProvider(filterProvider);

        Person p = Person.of("yy", Address.of("湖南岳阳", "岳阳县", 33));
        System.out.println(p);
        System.out.println("-- serializing --");
        String s = om.writeValueAsString(p);
        System.out.println(s);
    }

    @SuppressWarnings("unused")
    private static class Address {
        private String street;
        private String city;
        private int zipCode;

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

        public int getZipCode() {
            return zipCode;
        }

        public void setZipCode(int zipCode) {
            this.zipCode = zipCode;
        }

        public static Address of(String street, String city, int zipCode) {
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
    @JsonFilter("empFilter")
    private static class Employee {
        private String name;
        private String dept;
        private int salary;
        private String phone;

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

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public static Employee of(String name, String dept, int salary, String phone) {
            Employee e = new Employee();
            e.name = name;
            e.dept = dept;
            e.salary = salary;
            e.phone = phone;
            return e;
        }

        @Override
        public String toString() {
            return "Employee{" + "name='" + name + '\'' + ", dept='" + dept + '\'' + ", salary=" + salary + ", phone='"
                + phone + '\'' + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class Person {
        private String name;
        @JsonFilter("PersonAddressFilter")
        private Address address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public static Person of(String name, Address address) {
            Person p = new Person();
            p.name = name;
            p.address = address;
            return p;
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", address=" + address + '}';
        }
    }
}
