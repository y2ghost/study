package study.ywork.jpa.model.inheritance.embeddable;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Item {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )

    protected String name;

    protected Dimensions dimensions;

    protected Weight weight;

    public Item() {
    }

    public Item(String name, Dimensions dimensions, Weight weight) {
        this.name = name;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public Long getId() { // Optional but useful
        return id;
    }

    public String getName() {
        return name;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }
}
