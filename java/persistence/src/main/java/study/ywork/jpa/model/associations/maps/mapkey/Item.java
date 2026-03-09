package study.ywork.jpa.model.associations.maps.mapkey;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Item {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    protected String name;

    @MapKey(name = "id")
    @OneToMany(mappedBy = "item")
    protected Map<Long, Bid> bids = new HashMap<>();

    public Item() {
    }

    public Item(String name) {
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

    public Map<Long, Bid> getBids() {
        return bids;
    }
}
