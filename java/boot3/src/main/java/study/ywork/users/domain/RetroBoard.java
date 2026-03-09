package study.ywork.users.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class RetroBoard {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @NotBlank(message = "A name must be provided")
    private String name;

    @OneToMany(mappedBy = "retroBoard")
    private List<Card> cards;

    public RetroBoard() {
        // 不做事儿
    }

    public RetroBoard(String name) {
        this.name = name;
    }

    public RetroBoard(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public RetroBoard card(Card card) {
        if (null == cards) {
            cards = new ArrayList<>();
        }

        cards.add(card);
        return this;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
