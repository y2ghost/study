package study.ywork.jpa.model.complexschemas.secondarytable;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
@SecondaryTable(
        name = "BILLING_ADDRESS",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "USER_ID")
)
public class User {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    protected Address homeAddress;

    @AttributeOverride(name = "street",
            column = @Column(table = "BILLING_ADDRESS",
                    nullable = false))
    @AttributeOverride(name = "zipcode",
            column = @Column(table = "BILLING_ADDRESS",
                    length = 5,
                    nullable = false))
    @AttributeOverride(name = "city",
            column = @Column(table = "BILLING_ADDRESS",
                    nullable = false))
    protected Address billingAddress;

    public Long getId() {
        return id;
    }

    protected String username;

    public User() {
        // NOOP
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }
}
