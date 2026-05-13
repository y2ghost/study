package study.ywork.hibernate.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "person_one")
public class PersonOne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_detail_id")
    private PassportDetailOne passportDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PassportDetailOne getPassportDetail() {
        return passportDetail;
    }

    public void setPassportDetail(PassportDetailOne passportDetail) {
        this.passportDetail = passportDetail;
    }

    @Override
    public String toString() {
        return "PersonOne [id=" + id + ", name=" + name + ", passportDetail=" + passportDetail + "]";
    }
}
