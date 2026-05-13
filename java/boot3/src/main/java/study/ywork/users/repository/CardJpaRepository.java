package study.ywork.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.ywork.users.domain.Card;

import java.util.UUID;

public interface CardJpaRepository extends JpaRepository<Card, UUID> {
}
