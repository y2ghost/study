package study.ywork.users.service;

import org.springframework.stereotype.Service;
import study.ywork.users.domain.Card;
import study.ywork.users.domain.RetroBoard;
import study.ywork.users.exception.CardNotFoundException;
import study.ywork.users.exception.RetroBoardNotFoundException;
import study.ywork.users.repository.CardJpaRepository;
import study.ywork.users.repository.RetroBoardJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RetroBoardService {
    private final RetroBoardJpaRepository retroBoardRepository;
    private final CardJpaRepository cardRepository;

    public RetroBoardService(RetroBoardJpaRepository retroBoardRepository,
                             CardJpaRepository cardRepository) {
        this.retroBoardRepository = retroBoardRepository;
        this.cardRepository = cardRepository;
    }


    public RetroBoard save(RetroBoard domain) {
        return this.retroBoardRepository.save(domain);
    }

    public RetroBoard findById(UUID uuid) {
        return this.retroBoardRepository.findById(uuid).orElseThrow(RetroBoardNotFoundException::new);
    }

    public Iterable<RetroBoard> findAll() {
        return this.retroBoardRepository.findAll();
    }

    public void delete(UUID uuid) {
        this.retroBoardRepository.deleteById(uuid);
    }

    public Iterable<Card> findAllCardsFromRetroBoard(UUID uuid) {
        return this.findById(uuid).getCards();
    }

    public Card addCardToRetroBoard(UUID uuid, Card card) {
        return retroBoardRepository.findById(uuid).map(retroBoard -> {
            card.setRetroBoard(retroBoard);
            return cardRepository.save(card);
        }).orElseThrow(RetroBoardNotFoundException::new);
    }

    public void addMultipleCardsToRetroBoard(UUID uuid, List<Card> cards) {
        RetroBoard retroBoard = this.findById(uuid);
        cards.forEach(card -> card.setRetroBoard(retroBoard));
        cardRepository.saveAll(cards);
    }

    public Card findCardByUUID(UUID uuidCard) {
        Optional<Card> result = cardRepository.findById(uuidCard);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new CardNotFoundException();
        }
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    public void removeCardByUUID(UUID cardUUID) {
        cardRepository.deleteById(cardUUID);
    }
}
