package site.hospital.repository.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.Question;

import java.util.List;

public interface QuestionRepositoryCustom {
    public List<Question> searchHospitalQuestion(Long hospitalId);
    public Page<Question> searchQuestion(Long memberId, Long hospitalId, Pageable pageable);
}
