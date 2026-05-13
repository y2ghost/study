package study.ywork.users.repository;

import org.springframework.data.repository.CrudRepository;
import study.ywork.users.domain.RetroBoard;

import java.util.UUID;

public interface RetroBoardCrudRepository extends CrudRepository<RetroBoard, UUID> {
}
