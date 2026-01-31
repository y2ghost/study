package study.ywork.hibernate.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "employee_one")
public class EmployeeOne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "employee_detail",
        joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "detail_id", referencedColumnName = "id"))
    private DetailOne employeeDetail;

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

    public DetailOne getEmployeeDetail() {
        return employeeDetail;
    }

    public void setEmployeeDetail(DetailOne employeeDetail) {
        this.employeeDetail = employeeDetail;
    }

    @Override
    public String toString() {
        return "EmployeeOne [id=" + id + ", name=" + name + ", employeeDetail=" + employeeDetail + "]";
    }
}
