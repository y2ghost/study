package study.ywork.jpa.model.associations.manytomany.ternary;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Embeddable
public class CategorizedItem {
    @ManyToOne
    @JoinColumn(
            name = "ITEM_ID",
            nullable = false, updatable = false
    )
    protected Item item;

    @ManyToOne
    @JoinColumn(
            name = "USER_ID",
            updatable = false
    )
    @NotNull
    protected User addedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @NotNull
    protected Date addedOn = new Date();

    protected CategorizedItem() {
    }

    public CategorizedItem(User addedBy,
                           Item item) {
        this.addedBy = addedBy;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategorizedItem that = (CategorizedItem) o;
        if (!addedBy.equals(that.addedBy)) return false;
        if (!addedOn.equals(that.addedOn)) return false;
        return item.equals(that.item);
    }

    @Override
    public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + addedBy.hashCode();
        result = 31 * result + addedOn.hashCode();
        return result;
    }
}
