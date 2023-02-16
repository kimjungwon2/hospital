package site.hospital.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.question.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>,
        QuestionRepositoryCustom {

}
