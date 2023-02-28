package site.hospital.question.manager.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtService;
import site.hospital.question.manager.repository.dto.ManagerQuestionSearchCondition;
import site.hospital.question.user.api.dto.QuestionSearchResponse;
import site.hospital.question.user.domain.Question;
import site.hospital.question.user.repository.QuestionRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerQuestionServiceImpl implements ManagerQuestionService {

    private final QuestionRepository questionRepository;
    private final ManagerJwtService managerJwtService;

    @Override
    public Page<Question> searchHospitalQuestions(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        Page<Question> questions = getHospitalQuestions(servletRequest, nickName, memberIdName, pageable);

        return getPagingQuestions(pageable, questions);
    }

    @Override
    public Page<Question> searchNoQuestions(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        Page<Question> questions = getNoQuestions(servletRequest, nickName, memberIdName, pageable);

        return getPagingQuestions(pageable, questions);
    }

    @Override
    public Long countQuestionsWithNoAnswer(ServletRequest servletRequest) {
        Long JwtHospitalId = managerJwtService.getHospitalNumber(servletRequest);

        return questionRepository.managerCountQuestionsWithNoAnswer(JwtHospitalId);
    }

    private PageImpl getPagingQuestions(Pageable pageable, Page<Question> questions) {
        List<QuestionSearchResponse> result =
                questions
                        .stream()
                        .map(q -> QuestionSearchResponse.from(q))
                        .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

    private Page<Question> getNoQuestions(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        ManagerQuestionSearchCondition condition =
                ManagerQuestionSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .build();

        Long JwtHospitalId = managerJwtService.getHospitalNumber(servletRequest);

        Page<Question> questions = questionRepository
                .managerSearchNoQuestion(JwtHospitalId, condition, pageable);

        return questions;
    }

    private Page<Question> getHospitalQuestions(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName, Pageable pageable
    ) {
        ManagerQuestionSearchCondition condition =
                ManagerQuestionSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .build();

        Long JwtHospitalId = managerJwtService.getHospitalNumber(servletRequest);

        Page<Question> questions = questionRepository
                .managerSearchHospitalQuestion(JwtHospitalId, condition, pageable);

        return questions;
    }

}
