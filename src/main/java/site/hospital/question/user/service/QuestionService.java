package site.hospital.question.user.service;

import java.util.List;
import site.hospital.question.user.api.dto.QuestionCreateRequest;
import site.hospital.question.user.api.dto.QuestionCreateResponse;
import site.hospital.question.user.repository.hospital.HospitalQuestionSelectQuery;
import site.hospital.question.user.repository.inquiry.UserQuestionSelectQuery;

public interface QuestionService {

    QuestionCreateResponse createQuestion(QuestionCreateRequest request);

    List<UserQuestionSelectQuery> inquireQuestionsByUser(Long memberId);

    List<HospitalQuestionSelectQuery> inquireHospitalQuestions(Long hospitalId);

}
