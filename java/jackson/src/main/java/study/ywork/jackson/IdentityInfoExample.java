package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/*
 * 使用@JsonIdentityInfo处理循环引用
 */

public class IdentityInfoExample {
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
        customer.setName("Peter");
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
        Employee emp = new Employee();
        emp.setEmpId(1);

        Dept dept1 = new Dept();
        dept1.setDeptId(3);
        dept1.setEmployees(List.of(emp));

        Dept dept2 = new Dept();
        dept2.setDeptId(4);
        dept2.setEmployees(List.of(emp));

        System.out.println("-- 序列化之前 --");
        System.out.println(emp);

        System.out.println("-- 序列化之后 --");
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(emp);
        System.out.println(s);
        System.out.println("-- 反序列化 --");
        Employee employee = om.readValue(s, Employee.class);
        System.out.println(employee);
        System.out.println("-- deserialized back references --");
        for (Dept dept : employee.getDepts()) {
            System.out.println(dept + " -> " + dept.getEmployees());
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
    private static class Dept {
        private Integer deptId;
        List<Employee> employees;

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
                List<Dept> depts = a.getDepts();
                if (a.getDepts() == null) {
                    depts = new ArrayList<>();
                    a.setDepts(depts);
                }

                depts.add(this);
            }
        }

        @Override
        public String toString() {
            return "Dept{" + "deptId=" + deptId + '}';
        }
    }

    @SuppressWarnings("unused")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "empId")
    private static class Employee {
        private Integer empId;
        private List<Dept> depts;

        public Integer getEmpId() {
            return empId;
        }

        public void setEmpId(Integer empId) {
            this.empId = empId;
        }

        public List<Dept> getDepts() {
            return depts;
        }

        public void setDepts(List<Dept> depts) {
            this.depts = depts;
        }

        @Override
        public String toString() {
            return "Employee{" + "empId=" + empId + ", depts=" + depts + '}';
        }
    }
}
