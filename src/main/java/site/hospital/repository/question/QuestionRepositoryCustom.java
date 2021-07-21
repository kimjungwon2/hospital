package site.hospital.repository.question;

import site.hospital.domain.Question;

import java.util.List;

public interface QuestionRepositoryCustom {
    public List<Question> searchHospitalQuestion(Long hospitalId);
    public List<Question> searchQuestion(Long memberId, Long hospitalId);
}
