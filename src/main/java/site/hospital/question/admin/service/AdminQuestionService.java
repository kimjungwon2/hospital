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
import site.hospital.question.user.repository.adminSearchQuery.AdminQuestionSearchRepository;
import site.hospital.question.user.repository.adminSearchQuery.AdminSearchQuestionDto;
import site.hospital.question.user.repository.dto.AdminQuestionSearchCondition;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminQuestionService {

    private final QuestionRepository questionRepository;
    private final AdminQuestionSearchRepository adminQuestionSearchRepository;
    private final AnswerRepository answerRepository;


    //관리자 병원 Question 검색
    public Page<AdminSearchQuestionDto> adminSearchQuestions(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    ) {
        AdminQuestionSearchCondition condition = AdminQuestionSearchCondition.builder()
                .nickName(nickName).hospitalName(hospitalName).memberIdName(memberIdName).build();
        return adminQuestionSearchRepository.adminSearchQuestions(condition, pageable);
    }

    //관리자 병원 Question 삭제
    @Transactional
    public void questionDelete(Long questionId, Long answerId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 질문이 존재하지 않습니다."));
        questionRepository.deleteById(questionId);

        //Answer가 있으면 answer도 삭제.
        if (answerId != null) {
            Answer answer = answerRepository.findById(answerId)
                    .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 답변이 존재하지 않습니다."));
            answerRepository.deleteById(answerId);
        }
    }

}
