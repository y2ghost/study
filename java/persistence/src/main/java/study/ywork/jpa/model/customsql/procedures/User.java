package study.ywork.jpa.model.customsql.procedures;

import org.hibernate.annotations.ResultCheckStyle;
import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@NamedNativeQuery(
        name = "findUserById",
        query = "{call FIND_USER_BY_ID(?)}",
        resultClass = User.class
)
@org.hibernate.annotations.Loader(
        namedQuery = "findUserById"
)
@org.hibernate.annotations.SQLInsert(
        sql = "{call INSERT_USER(?, ?, ?)}",
        callable = true
)
@org.hibernate.annotations.SQLUpdate(
        sql = "{call UPDATE_USER(?, ?, ?)}",
        callable = true,
        check = ResultCheckStyle.NONE
)
@org.hibernate.annotations.SQLDelete(
        sql = "{call DELETE_USER(?)}",
        callable = true
)
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    protected String username;

    protected boolean activated = true;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
