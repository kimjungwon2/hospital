package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
}
