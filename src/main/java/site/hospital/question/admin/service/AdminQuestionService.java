package site.hospital.question.admin.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.answer.manager.domain.Answer;
import site.hospital.answer.manager.repository.AnswerRepository;
import site.hospital.question.user.domain.Question;
import site.hospital.question.user.repository.QuestionRepository;
import site.hospital.question.admin.repository.search.AdminQuestionSearchRepository;
import site.hospital.question.admin.repository.search.AdminQuestionSearchSelectQuery;
import site.hospital.question.admin.repository.dto.AdminQuestionSearchCondition;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminQuestionService {

    private final QuestionRepository questionRepository;
    private final AdminQuestionSearchRepository adminQuestionSearchRepository;
    private final AnswerRepository answerRepository;


    public Page<AdminQuestionSearchSelectQuery> searchQuestions(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    ) {
        AdminQuestionSearchCondition condition =
                AdminQuestionSearchCondition
                .builder()
                .nickName(nickName)
                .hospitalName(hospitalName)
                .memberIdName(memberIdName)
                .build();

        return adminQuestionSearchRepository.adminSearchQuestions(condition, pageable);
    }


    @Transactional
    public void deleteQuestion(Long questionId, Long answerId) {
        questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 질문이 존재하지 않습니다."));
        questionRepository.deleteById(questionId);

        if (answerId != null) {
            deleteWithAnswer(answerId);
        }
    }

    private void deleteWithAnswer(Long answerId) {
        answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 답변이 존재하지 않습니다."));
        answerRepository.deleteById(answerId);
    }

}
