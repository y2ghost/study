package study.ywork.jpa.model.associations.manytomany.linkentity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CATEGORY_ITEM")
@org.hibernate.annotations.Immutable
public class CategorizedItem {
    @Embeddable
    public static class Id implements Serializable {

        @Column(name = "CATEGORY_ID")
        protected Long categoryId;

        @Column(name = "ITEM_ID")
        protected Long itemId;

        public Id() {
        }

        public Id(Long categoryId, Long itemId) {
            this.categoryId = categoryId;
            this.itemId = itemId;
        }

        public boolean equals(Object o) {
            if (o instanceof Id that) {
                return this.categoryId.equals(that.categoryId)
                        && this.itemId.equals(that.itemId);
            }
            return false;
        }

        public int hashCode() {
            return categoryId.hashCode() + itemId.hashCode();
        }
    }

    @EmbeddedId
    protected Id id = new Id();

    @Column(updatable = false)
    @NotNull
    protected String addedBy;

    @Column(updatable = false)
    @NotNull
    protected Date addedOn = new Date();

    @ManyToOne
    @JoinColumn(
            name = "CATEGORY_ID",
            insertable = false, updatable = false)
    protected Category category;

    @ManyToOne
    @JoinColumn(
            name = "ITEM_ID",
            insertable = false, updatable = false)
    protected Item item;


    public CategorizedItem() {
    }

    public CategorizedItem(String addedByUsername,
                           Category category,
                           Item item) {
        this.addedBy = addedByUsername;
        this.category = category;
        this.item = item;

        // Set identifier values
        this.id.categoryId = category.getId();
        this.id.itemId = item.getId();
        category.getCategorizedItems().add(this);
        item.getCategorizedItems().add(this);
    }

    public Id getId() {
        return id;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public Category getCategory() {
        return category;
    }

    public Item getItem() {
        return item;
    }
}
