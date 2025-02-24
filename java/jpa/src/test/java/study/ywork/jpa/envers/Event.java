package study.ywork.jpa.envers;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "EnverEvent")
@Table(name = "Events3")
@Audited
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(name = "eventDate")
    private LocalDateTime date;

    public Event() {
        // 不做事儿
    }

    public Event(String title, LocalDateTime date) {
        this.title = title;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
