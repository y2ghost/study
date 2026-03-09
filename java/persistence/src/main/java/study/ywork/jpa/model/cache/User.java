package study.ywork.jpa.model.cache;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
@Cacheable
@org.hibernate.annotations.Cache(
        usage = org.hibernate.annotations
                .CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,
        region = "study.ywork.jpa.model.cache.User"
)
@org.hibernate.annotations.NaturalIdCache
public class User {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    @org.hibernate.annotations.NaturalId(mutable = true)
    @Column(nullable = false)
    protected String username;

    protected User() {
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
}
