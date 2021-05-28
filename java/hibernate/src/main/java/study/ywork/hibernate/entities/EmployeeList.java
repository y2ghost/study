package study.ywork.hibernate.entities;

import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/*
 * 雇员信息
 * 测试保存List对象
 */
@Entity
@Table(name = "employee_list")
public class EmployeeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "email_list")
    @OrderColumn(name = "sequence")
    private List<String> emails;

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

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        return "EmployeeList [id=" + id + ", name=" + name + ", emails=" + emails + "]";
    }
}
