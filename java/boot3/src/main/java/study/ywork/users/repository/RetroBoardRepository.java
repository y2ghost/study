package study.ywork.users.repository;

import org.springframework.stereotype.Component;
import study.ywork.users.domain.Card;
import study.ywork.users.domain.CardType;
import study.ywork.users.domain.RetroBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class RetroBoardRepository implements SimpleRepository<RetroBoard, UUID> {
    private final Map<UUID, RetroBoard> retroBoardMap = new HashMap<>();

    public RetroBoardRepository() {
        UUID retroBoardId = UUID.fromString("9DC9B71B-A07E-418B-B972-40225449AFF2");
        retroBoardMap.put(retroBoardId,
                new RetroBoard(UUID.fromString("9DC9B71B-A07E-418B-B972-40225449AFF2"),
                        "Spring Boot 3.0 Meeting")
                        .card(new Card(UUID.fromString("BB2A80A5-A0F5-4180-A6DC-80C84BC014C9"),
                                "Happy to meet the team",
                                CardType.HAPPY))
                        .card(new Card(UUID.fromString("011EF086-7645-4534-9512-B9BC4CCFB688"),
                                "New projects",
                                CardType.HAPPY))
                        .card(new Card(UUID.fromString("775A3905-D6BE-49AB-A3C4-EBE287B51539"),
                                "When to meet again??",
                                CardType.MEH))
                        .card(new Card(UUID.fromString("896C093D-1C50-49A3-A58A-6F1008789632"),
                                "We need more time to finish",
                                CardType.SAD))
        );
    }

    @Override
    public RetroBoard save(RetroBoard domain) {
        if (null == domain.getId()) {
            domain.setId(UUID.randomUUID());
        }

        this.retroBoardMap.put(domain.getId(), domain);
        return domain;
    }

    @Override
    public Optional<RetroBoard> findById(UUID uuid) {
        return Optional.ofNullable(this.retroBoardMap.get(uuid));
    }

    @Override
    public Iterable<RetroBoard> findAll() {
        return this.retroBoardMap.values();
    }

    @Override
    public void deleteById(UUID uuid) {
        this.retroBoardMap.remove(uuid);
    }
}
