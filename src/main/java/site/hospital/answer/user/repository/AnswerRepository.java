package site.hospital.answer.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.answer.user.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
