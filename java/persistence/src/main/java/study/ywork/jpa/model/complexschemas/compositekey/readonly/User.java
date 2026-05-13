package study.ywork.jpa.model.complexschemas.compositekey.readonly;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
    @EmbeddedId
    protected UserId id;

    @ManyToOne
    @JoinColumn(
            name = "DEPARTMENTID",
            insertable = false, updatable = false
    )
    protected Department department;

    public User(UserId id) {
        this.id = id;
    }

    public User(String username, Department department) {
        if (department.getId() == null)
            throw new IllegalStateException(
                    "Department is transient: " + department
            );
        this.id = new UserId(username, department.getId());
        this.department = department;
    }

    protected User() {
        // NOOP
    }

    public UserId getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }
}