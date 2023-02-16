package site.hospital.answer.manager.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.answer.manager.api.dto.AnswerCreateRequest;
import site.hospital.answer.manager.domain.Answer;
import site.hospital.answer.manager.repository.AnswerRepository;
import site.hospital.question.domain.Question;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.repository.MemberRepository;
import site.hospital.question.repository.QuestionRepository;
import site.hospital.common.service.ManagerJwtAccessService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerAnswerService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ManagerJwtAccessService managerJwtAccessService;

    //답변 등록
    @Transactional
    public Long registerAnswer(
            ServletRequest servletRequest,
            AnswerCreateRequest request
    ) {
        Member member = memberRepository
                .findById(request.getMemberId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        Question question = questionRepository
                .findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 질문이 존재하지 않습니다."));

        managerJwtAccessService
                .staffAccessFunction(servletRequest, member.getId(),
                        question.getHospital().getId());

        Answer answer = Answer.builder().answerContent(request.getAnswerContent()).build();

        answer.changeMember(member);

        answerRepository.save(answer);
        question.changeAnswer(answer);

        return answer.getId();
    }
}