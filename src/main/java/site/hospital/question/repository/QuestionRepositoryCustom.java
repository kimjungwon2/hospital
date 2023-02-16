package site.hospital.question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.question.domain.Question;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.question.repository.dto.StaffQuestionSearchCondition;

public interface QuestionRepositoryCustom {

    Page<Question> searchQuestion(Long memberId, Pageable pageable);

    void adminDeleteQuestion(Hospital hospital);

    Page<Question> staffSearchHospitalQuestion(Long hospitalId,
            StaffQuestionSearchCondition condition, Pageable pageable);

    Page<Question> staffSearchNoQuestion(Long hospitalId, StaffQuestionSearchCondition condition,
            Pageable pageable);

    Long staffQuestionNoAnswer(Long hospitalId);
}
