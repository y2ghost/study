package study.ywork.jpa.model.complexschemas.compositekey.manytoone;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {
    protected String username;
    protected String departmentNr;

    protected UserId() {
        // NOOP
    }

    public UserId(String username, String departmentNr) {
        this.username = username;
        this.departmentNr = departmentNr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartmentNr() {
        return departmentNr;
    }

    public void setDepartmentNr(String departmentNr) {
        this.departmentNr = departmentNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        if (!departmentNr.equals(userId.departmentNr)) return false;
        return username.equals(userId.username);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + departmentNr.hashCode();
        return result;
    }
}
