package site.hospital.repository.question;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>,
        QuestionRepositoryCustom {

}
