package study.ywork.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.ywork.users.domain.RetroBoard;

import java.util.UUID;

public interface RetroBoardJpaRepository extends JpaRepository<RetroBoard, UUID> {
}
