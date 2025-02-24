package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

/*
 * 使用@JsonManagedReference和@JsonBackReference处理循环引用
 */

public class BackReferenceExample {
    public static void main(String[] args) throws IOException {
        orderTest();
        System.out.println();
        employeeTest();
        System.out.println();
    }

    public static void orderTest() throws IOException {
        Order order = new Order();
        order.setOrderId(1);
        order.setItemIds(List.of(10, 30));

        Customer customer = new Customer();
        customer.setId(2);
        customer.setName("yy");
        customer.setOrder(order);
        order.setCustomer(customer);

        System.out.println(customer);
        System.out.println("-- 序列化 --");
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(customer);
        System.out.println(s);
        System.out.println("-- 反序列化 --");
        Customer customer2 = om.readValue(s, Customer.class);
        System.out.println(customer2);
    }

    public static void employeeTest() throws IOException {
        Employee emp1 = new Employee();
        emp1.setEmpId(1);
        emp1.setName("Tina");

        Employee emp2 = new Employee();
        emp2.setEmpId(2);
        emp2.setName("Joe");

        Dept dept = new Dept();
        dept.setName("IT");
        dept.setDeptId(3);
        dept.setEmployees(List.of(emp1, emp2));

        System.out.println("-- 序列化之前 --");
        System.out.println(dept);

        System.out.println("-- 序列化之后 --");
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(dept);
        System.out.println(s);
        System.out.println("-- 反序列化 --");
        Dept dept1 = om.readValue(s, Dept.class);
        System.out.println(dept1);
    }

    @SuppressWarnings("unused")
    private static class Order {
        private Integer orderId;
        private List<Integer> itemIds;
        @JsonBackReference
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
    private static class Customer {
        private Integer id;
        private String name;
        @JsonManagedReference
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
            return "Customer [id=" + id + ", name=" + name + ", order=" + order + "]";
        }
    }

    @SuppressWarnings("unused")
    private static class Dept {
        private int deptId;
        @JsonManagedReference
        List<Employee> employees;
        private String name;

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public List<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(List<Employee> employeeList) {
            this.employees = employeeList;
            for (Employee a : employeeList) {
                a.setDept(this);
            }
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Dept{" + "deptId=" + deptId + ", employees=" + employees + ", name='" + name + '\'' + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private Integer empId;
        @JsonBackReference
        private Dept dept;
        private String name;

        public int getEmpId() {
            return empId;
        }

        public void setEmpId(int empId) {
            this.empId = empId;
        }

        public Dept getDept() {
            return dept;
        }

        public void setDept(Dept dept) {
            this.dept = dept;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Employee{" + "empId=" + empId + ", deptId=" + dept.getDeptId() + ", name='" + name + '\'' + '}';
        }
    }
}
