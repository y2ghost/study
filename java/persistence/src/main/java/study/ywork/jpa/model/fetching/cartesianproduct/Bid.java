package study.ywork.jpa.model.fetching.cartesianproduct;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Bid {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected Item item;

    @NotNull
    protected BigDecimal amount;

    public Bid() {
    }

    public Bid(Item item, BigDecimal amount) {
        this.item = item;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}