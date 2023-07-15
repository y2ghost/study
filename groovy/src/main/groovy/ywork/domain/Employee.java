package ywork.domain;

public class Employee {
    String name;
    String dept;

    public Employee() {
    }

    public Employee(String name, String dept) {
        this.name = name;
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "Employee [name=" + name + ", dept=" + dept + "]";
    }
}
