package study.ywork.users.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import study.ywork.users.domain.Card;
import study.ywork.users.domain.RetroBoard;
import study.ywork.users.service.RetroBoardService;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/retros")
public class RetroBoardController {
    private final RetroBoardService retroBoardService;

    public RetroBoardController(RetroBoardService retroBoardService) {
        this.retroBoardService = retroBoardService;
    }

    @GetMapping
    public ResponseEntity<Iterable<RetroBoard>> getAllRetroBoards() {
        return ResponseEntity.ok(retroBoardService.findAll());
    }

    @PostMapping
    public ResponseEntity<RetroBoard> saveRetroBoard(@Valid @RequestBody RetroBoard retroBoard) {
        RetroBoard result = retroBoardService.save(retroBoard);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(result.getId().toString())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RetroBoard> findRetroBoardById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(retroBoardService.findById(uuid));
    }

    @GetMapping("/{uuid}/cards")
    public ResponseEntity<Iterable<Card>> getAllCardsFromBoard(@PathVariable UUID uuid) {
        return ResponseEntity.ok(retroBoardService.findAllCardsFromRetroBoard(uuid));
    }

    @PutMapping("/{uuid}/cards")
    public ResponseEntity<Card> addCardToRetroBoard(@PathVariable UUID uuid, @Valid @RequestBody Card card) {
        Card result = retroBoardService.addCardToRetroBoard(uuid, card);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}/cards/{uuidCard}")
                .buildAndExpand(uuid.toString(), result.getId().toString())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping("/cards/{uuidCard}")
    public ResponseEntity<Card> getCardByUUID(@PathVariable UUID uuidCard) {
        return ResponseEntity.ok(retroBoardService.findCardByUUID(uuidCard));
    }

    @PutMapping("/cards/{uuidCard}")
    public ResponseEntity<Card> updateCardByUUID(@PathVariable UUID uuidCard, @RequestBody Card card) {
        Card result = retroBoardService.findCardByUUID(uuidCard);
        result.setComment(card.getComment());
        return ResponseEntity.ok(retroBoardService.saveCard(result));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cards/{uuidCard}")
    public void deleteCardFromRetroBoard(@PathVariable UUID uuidCard) {
        retroBoardService.removeCardByUUID(uuidCard);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("msg", "There is an error");
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("errors", errors);
        return response;
    }
}
