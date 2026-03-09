package study.ywork.jpa.model.complexschemas.compositekey.mapsid;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Department {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @NotNull
    protected String name;

    public Department() {
        // NOOP
    }

    public Department(String name) {
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
}
