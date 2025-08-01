package study.ywork.jpa.model.fetching.proxy;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class User {
    protected Long id;
    protected String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}