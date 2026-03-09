package study.ywork.jpa.model.customsql;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@NamedNativeQuery(
        name = "findUserById",
        query = "select * from USERS where ID = ?",
        resultClass = User.class
)
@org.hibernate.annotations.Loader(
        namedQuery = "findUserById"
)
@org.hibernate.annotations.SQLInsert(
        sql = "insert into USERS " +
                "(ACTIVATED, USERNAME, ID) values (?, ?, ?)"
)
@org.hibernate.annotations.SQLUpdate(
        sql = "update USERS set " +
                "ACTIVATED = ?, " +
                "USERNAME = ? " +
                "where ID = ?"
)
@org.hibernate.annotations.SQLDelete(
        sql = "delete from USERS where ID = ?"
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
