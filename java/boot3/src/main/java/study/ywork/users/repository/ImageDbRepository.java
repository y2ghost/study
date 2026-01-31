package study.ywork.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.ywork.users.domain.Image;

@Repository
public
interface ImageDbRepository extends JpaRepository<Image, Long> {
}
