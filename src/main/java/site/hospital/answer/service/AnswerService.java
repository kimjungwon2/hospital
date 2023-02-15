package site.hospital.answer.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.answer.api.dto.AnswerCreateRequest;
import site.hospital.answer.domain.Answer;
import site.hospital.question.domain.Question;
import site.hospital.member.user.domain.Member;
import site.hospital.answer.repository.AnswerRepository;
import site.hospital.member.user.repository.MemberRepository;
import site.hospital.question.repository.QuestionRepository;
import site.hospital.common.service.JwtStaffAccessService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final JwtStaffAccessService jwtStaffAccessService;

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

        jwtStaffAccessService
                .staffAccessFunction(servletRequest, member.getId(),
                        question.getHospital().getId());

        Answer answer = Answer.builder().answerContent(request.getAnswerContent()).build();

        answer.changeMember(member);

        answerRepository.save(answer);
        question.changeAnswer(answer);

        return answer.getId();
    }
}
