package study.ywork.quartz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.ywork.quartz.domain.ApplicationJob;

@Repository
public interface ApplicationJobRepository extends JpaRepository<ApplicationJob, Long> {
}
