package study.ywork.users.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import study.ywork.users.domain.Card;
import study.ywork.users.domain.CardType;
import study.ywork.users.domain.RetroBoard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RetroBoardRowMapper implements RowMapper<RetroBoard> {
    @Override
    public RetroBoard mapRow(ResultSet rs, int rowNum) throws SQLException {
        RetroBoard retroBoard = new RetroBoard();
        retroBoard.setId(UUID.fromString(rs.getString("id")));
        retroBoard.setName(rs.getString("name"));
        Map<UUID, Card> cards = new HashMap<>();

        do {
            Card card = new Card();
            card.setId(UUID.fromString(rs.getString("card_id")));
            card.setComment(rs.getString("comment"));
            card.setCardType(CardType.valueOf(rs.getString("card_type")));
            card.setRetroBoard(retroBoard);
            cards.put(card.getId(), card);
        } while (rs.next() && retroBoard.getId().equals(UUID.fromString(rs.getString("id"))));

        retroBoard.setCards(new ArrayList<>(cards.values()));
        return retroBoard;
    }
}
