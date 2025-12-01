package study.ywork.users.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
public class Card {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @NotBlank(message = "A comment must be provided always")
    private String comment;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @ManyToOne
    @JoinColumn(name = "retro_board_id")
    @JsonIgnore
    private RetroBoard retroBoard;

    public Card() {
        // 不做事儿
    }

    public Card(UUID id, String comment, CardType cardType) {
        this.id = id;
        this.comment = comment;
        this.cardType = cardType;
    }

    public Card(CarBuilder builder) {
        this.id = builder.id;
        this.comment = builder.comment;
        this.cardType = builder.cardType;
        this.retroBoard = builder.retroBoard;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public RetroBoard getRetroBoard() {
        return retroBoard;
    }

    public void setRetroBoard(RetroBoard retroBoard) {
        this.retroBoard = retroBoard;
    }

    public static CarBuilder builder() {
        return new CarBuilder();
    }

    public static class CarBuilder {
        private UUID id;
        private String comment;
        private CardType cardType;
        private RetroBoard retroBoard;

        public CarBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CarBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public CarBuilder cardType(CardType cardType) {
            this.cardType = cardType;
            return this;
        }

        public CarBuilder retroBoard(RetroBoard retroBoard) {
            this.retroBoard = retroBoard;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
