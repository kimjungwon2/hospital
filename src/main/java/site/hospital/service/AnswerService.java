package site.hospital.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Answer;
import site.hospital.domain.Question;
import site.hospital.domain.member.Member;
import site.hospital.repository.AnswerRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.question.QuestionRepository;

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
    public Long registerAnswer(ServletRequest servletRequest, Long memberId, Long questionId,
            Answer answer) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 질문이 존재하지 않습니다."));

        jwtStaffAccessService
                .staffAccessFunction(servletRequest, memberId, question.getHospital().getId());
        answer.changeMember(member);
        answerRepository.save(answer);

        question.changeAnswer(answer);

        return answer.getId();
    }
}
