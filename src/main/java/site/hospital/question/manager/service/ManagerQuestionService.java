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
import site.hospital.common.service.ManagerJwtAccessService;
import site.hospital.question.user.api.dto.QuestionSearchResponse;
import site.hospital.question.user.domain.Question;
import site.hospital.question.user.repository.QuestionRepository;
import site.hospital.question.user.repository.dto.StaffQuestionSearchCondition;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerQuestionService {

    private final QuestionRepository questionRepository;
    private final ManagerJwtAccessService managerJwtAccessService;

    //병원 관계자 Question 검색
    public Page<Question> staffSearchHospitalQuestion(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        StaffQuestionSearchCondition condition = StaffQuestionSearchCondition
                .builder()
                .nickName(nickName)
                .memberIdName(memberIdName)
                .build();

        Long JwtHospitalId = managerJwtAccessService.getHospitalNumber(servletRequest);

        Page<Question> questions = questionRepository
                .staffSearchHospitalQuestion(JwtHospitalId, condition, pageable);

        List<QuestionSearchResponse> result = questions.stream()
                .map(q -> QuestionSearchResponse.from(q))
                .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

    //병원 관계자 미답변 Question 검색
    public Page<Question> staffSearchNoQuestion(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        StaffQuestionSearchCondition condition =
                StaffQuestionSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .build();

        Long JwtHospitalId = managerJwtAccessService.getHospitalNumber(servletRequest);

        Page<Question> questions = questionRepository
                .staffSearchNoQuestion(JwtHospitalId, condition, pageable);

        List<QuestionSearchResponse> result = questions.stream()
                .map(q -> QuestionSearchResponse.from(q))
                .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

    //병원 관계자 미답변 question 갯수 확인
    public Long staffQuestionNoAnswer(ServletRequest servletRequest) {
        Long JwtHospitalId = managerJwtAccessService.getHospitalNumber(servletRequest);

        return questionRepository.staffQuestionNoAnswer(JwtHospitalId);
    }

}
