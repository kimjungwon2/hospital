package site.hospital.question.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.question.user.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>,
        QuestionRepositoryCustom {
}
