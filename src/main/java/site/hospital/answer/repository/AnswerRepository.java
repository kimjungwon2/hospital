package site.hospital.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.answer.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
