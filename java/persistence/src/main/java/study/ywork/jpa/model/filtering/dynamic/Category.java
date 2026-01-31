package study.ywork.jpa.model.filtering.dynamic;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@org.hibernate.annotations.FilterDef(
        name = "limitByUserRank",
        parameters = {
                @org.hibernate.annotations.ParamDef(
                        name = "currentUserRank", type = "int"
                )
        }
)
@org.hibernate.annotations.FilterDef(
        name = "limitByUserRankDefault",
        defaultCondition =
                ":currentUserRank >= (" +
                        "select u.RANK from USERS u " +
                        "where u.ID = SELLER_ID" +
                        ")",
        parameters = {
                @org.hibernate.annotations.ParamDef(
                        name = "currentUserRank", type = "int"
                )
        }
)
public class Category {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    protected String name;

    @OneToMany(mappedBy = "category")
    @org.hibernate.annotations.Filter(
            name = "limitByUserRank",
            condition =
                    ":currentUserRank >= (" +
                            "select u.RANK from USERS u " +
                            "where u.ID = SELLER_ID" +
                            ")"
    )
    protected Set<Item> items = new HashSet<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}