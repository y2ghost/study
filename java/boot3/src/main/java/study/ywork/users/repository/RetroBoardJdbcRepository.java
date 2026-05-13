package study.ywork.users.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.ywork.users.domain.Card;
import study.ywork.users.domain.RetroBoard;
import study.ywork.users.repository.mapper.RetroBoardRowMapper;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RetroBoardJdbcRepository implements SimpleRepository<RetroBoard, UUID> {
    private final JdbcTemplate jdbcTemplate;

    public RetroBoardJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<RetroBoard> findById(UUID uuid) {
        String sql = """
                SELECT r.ID AS id, r.NAME, c.ID AS card_id, c.CARD_TYPE AS card_type, c.COMMENT AS comment
                FROM retro_board r
                LEFT JOIN card c ON r.ID = c.RETRO_BOARD_ID
                WHERE r.ID = ?
                """;
        List<RetroBoard> results = jdbcTemplate.query(sql, new Object[]{uuid}, new int[]{Types.OTHER}, new RetroBoardRowMapper());
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Iterable<RetroBoard> findAll() {
        String sql = """
                SELECT r.ID AS id, r.NAME, c.ID AS card_id, c.CARD_TYPE, c.COMMENT
                FROM retro_board r
                LEFT JOIN card c ON r.ID = c.RETRO_BOARD_ID
                """;
        return jdbcTemplate.query(sql, new RetroBoardRowMapper());
    }

    @Override
    @Transactional
    public RetroBoard save(RetroBoard retroBoard) {
        if (null == retroBoard.getId()) {
            retroBoard.setId(UUID.randomUUID());
        }

        String sql = "INSERT INTO retro_board (ID, NAME) VALUES (?, ?)";
        jdbcTemplate.update(sql, retroBoard.getId(), retroBoard.getName());
        List<Card> cards = retroBoard.getCards();

        for (Card card : cards) {
            card.setRetroBoard(retroBoard);
            saveCard(card);
        }

        return retroBoard;
    }

    @Override
    @Transactional
    public void deleteById(UUID uuid) {
        String sql = "DELETE FROM card WHERE RETRO_BOARD_ID = ?";
        jdbcTemplate.update(sql, uuid);

        sql = "DELETE FROM retro_board WHERE ID = ?";
        jdbcTemplate.update(sql, uuid);
    }

    private Card saveCard(Card card) {
        if (null == card.getId()) {
            card.setId(UUID.randomUUID());
        }

        String sql = "INSERT INTO card (ID, CARD_TYPE, COMMENT, RETRO_BOARD_ID) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, card.getId(), card.getCardType().name(), card.getComment(), card.getRetroBoard().getId());
        return card;
    }
}
