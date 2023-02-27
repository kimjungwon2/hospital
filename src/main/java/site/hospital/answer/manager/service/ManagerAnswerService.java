package site.hospital.answer.manager.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.answer.manager.api.dto.AnswerCreateRequest;
import site.hospital.answer.user.domain.Answer;
import site.hospital.answer.user.repository.AnswerRepository;
import site.hospital.question.user.domain.Question;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.repository.MemberRepository;
import site.hospital.question.user.repository.QuestionRepository;
import site.hospital.common.service.ManagerJwtService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerAnswerService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ManagerJwtService managerJwtService;

    @Transactional
    public Long registerAnswer(
            ServletRequest servletRequest,
            AnswerCreateRequest request
    ) {
        Member member = memberRepository.findById(request.getMemberId())
                                        .orElseThrow(
                                                () -> new IllegalStateException("Member not exist"));

        Question question = questionRepository.findById(request.getQuestionId())
                                              .orElseThrow(
                                                      () -> new IllegalStateException("Question not exist"));

        managerJwtService.accessManager(servletRequest,
                                              member.getId(),
                                              question.getHospital().getId());

        Answer answer = createAnswer(request, member, question);

        return answer.getId();
    }

    private Answer createAnswer(AnswerCreateRequest request,
                               Member member,
                                Question question
    ) {
        Answer answer = Answer.builder()
                              .answerContent(request.getAnswerContent())
                              .build();

        answer.changeMember(member);
        answerRepository.save(answer);
        question.changeAnswer(answer);
        return answer;
    }
}
