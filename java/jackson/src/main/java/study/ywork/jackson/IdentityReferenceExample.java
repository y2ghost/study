package study.ywork.jackson;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 使用@JsonIdentityReference始终按ID序列化POJO
 * 配合@JsonIdentityInfo注解使用
 * @JsonIdentityInfo允许按ID序列化POJO，但仅当序列化过程中第二次遇到POJO时才如此
 * @JsonIdentityReference首次遇到POJO时，将其ID按ID序列化
 */
public class IdentityReferenceExample {
    private static final ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        orderTest();
        System.out.println();
        personTest();
        System.out.println();
        employeeTest();
        System.out.println();
    }

    private static void orderTest() throws IOException {
        Order order = new Order();
        order.setOrderId(1);
        order.setItemIds(List.of(10, 30));

        Customer customer = new Customer();
        customer.setId(2);
        customer.setName("yy");
        customer.setOrder(order);
        order.setCustomer(customer);
        System.out.println(customer);
        toJson(customer);
    }

    private static void personTest() throws IOException {
        Person person = new Person();
        person.setName("yy");
        person.setId(1);
        System.out.println(person);
        toJson(person);
    }

    private static void employeeTest() throws IOException {
        Employee employee = Employee.of("yy", "IT", Address.of(1, "111 Heaven Drive", "Sun Valley"));
        System.out.println(employee);
        toJson(employee);
    }

    private static void toJson(Object obj) throws IOException {
        System.out.println("-- 序列化 --");
        String s = om.writeValueAsString(obj);
        System.out.println(s);
    }

    @SuppressWarnings("unused")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private static class Address {
        private Integer id;
        private String street;
        private String city;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

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

        public static Address of(Integer id, String street, String city) {
            Address address = new Address();
            address.id = id;
            address.street = street;
            address.city = city;
            return address;
        }

        @Override
        public String toString() {
            return "Address{" + "street='" + street + '\'' + ", city='" + city + '\'' + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class Order {
        private Integer orderId;
        private List<Integer> itemIds;
        private Customer customer;

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public List<Integer> getItemIds() {
            return itemIds;
        }

        public void setItemIds(List<Integer> itemIds) {
            this.itemIds = itemIds;
        }

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        @Override
        public String toString() {
            return "Order{" + "id=" + orderId + ", itemIds=" + itemIds + '}';
        }
    }

    @SuppressWarnings("unused")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private static class Customer {
        private Integer id;
        private String name;
        private Order order;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        @Override
        public String toString() {
            return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", order=" + order + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        private String dept;
        @JsonIdentityReference(alwaysAsId = true)
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

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public static Employee of(String name, String dept, Address address) {
            Employee e = new Employee();
            e.name = name;
            e.dept = dept;
            e.address = address;
            return e;
        }

        @Override
        public String toString() {
            return "Employee{" + "name='" + name + '\'' + ", dept='" + dept + '\'' + ", address=" + address + '}';
        }
    }

    @SuppressWarnings("unused")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private static class Person {
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" + "id=" + id + ", name='" + name + '\'' + '}';
        }
    }
}
