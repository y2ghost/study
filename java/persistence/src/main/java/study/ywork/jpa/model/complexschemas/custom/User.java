package study.ywork.jpa.model.complexschemas.custom;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "USERS",
        uniqueConstraints = @UniqueConstraint(
                name = "UNQ_USERNAME_EMAIL",
                columnNames = {"email", "username"}
        ),
        indexes = {
                @Index(name = "IDX_USERNAME", columnList = "username"),
                @Index(name = "IDX_USERNAME_EMAIL", columnList = "username, username")
        }
)
public class User {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    protected String email;

    @Column(name = "username", columnDefinition = "varchar(15) not null unique" +
            " check (not substring(lower(USERNAME), 0, 5) = 'admin')"
    )
    protected String username;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
