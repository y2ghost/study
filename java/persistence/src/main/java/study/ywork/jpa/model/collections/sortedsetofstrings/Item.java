package study.ywork.jpa.model.collections.sortedsetofstrings;

import study.ywork.jpa.constant.CommonConstants;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class Item {
    @Id
    @GeneratedValue(generator = CommonConstants.ID_GENERATOR)
    protected Long id;

    @ElementCollection
    @CollectionTable(name = "IMAGE")
    @Column(name = "FILENAME")
    @org.hibernate.annotations.SortNatural
    protected SortedSet<String> images = new TreeSet<>();

    public Long getId() {
        return id;
    }

    public SortedSet<String> getImages() {
        return images;
    }

    public void setImages(SortedSet<String> images) {
        this.images = images;
    }
}
