package study.ywork.hibernate.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "passport_detail_two")
public class PassportDetailTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "passportno")
    private String passportno;

    @OneToOne(mappedBy = "passportDetail", cascade = CascadeType.ALL)
    private PersonTwo person;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassportno() {
        return passportno;
    }

    public void setPassportno(String passportno) {
        this.passportno = passportno;
    }

    public PersonTwo getPerson() {
        return person;
    }

    public void setPerson(PersonTwo person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "PassportDetailTwo [id=" + id + ", passportno=" + passportno + ", person=" + person + "]";
    }
}
