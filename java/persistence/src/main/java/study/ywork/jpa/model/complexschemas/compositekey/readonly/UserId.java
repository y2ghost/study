package study.ywork.jpa.model.complexschemas.compositekey.readonly;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {
    protected String username;
    protected Long departmentId;

    protected UserId() {
        // NOOP
    }

    public UserId(String username, Long departmentId) {
        this.username = username;
        this.departmentId = departmentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        if (!departmentId.equals(userId.departmentId)) return false;
        return username.equals(userId.username);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + departmentId.hashCode();
        return result;
    }
}
