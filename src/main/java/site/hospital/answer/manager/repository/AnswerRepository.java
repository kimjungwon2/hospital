package site.hospital.answer.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.answer.manager.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
