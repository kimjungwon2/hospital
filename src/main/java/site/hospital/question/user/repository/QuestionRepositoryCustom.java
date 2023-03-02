package site.hospital.question.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.question.user.domain.Question;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.question.manager.repository.dto.ManagerQuestionSearchCondition;

public interface QuestionRepositoryCustom {

    void adminDeleteQuestion(Hospital hospital);

    Page<Question> managerSearchHospitalQuestion(Long hospitalId,
            ManagerQuestionSearchCondition condition, Pageable pageable);

    Page<Question> managerSearchNoQuestions(Long hospitalId, ManagerQuestionSearchCondition condition,
            Pageable pageable);

    Long managerCountQuestionsWithNoAnswer(Long hospitalId);
}
