package site.hospital.service;

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
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class AnswerService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    //답변 등록
    @Transactional
    public Long registerAnswer(Long memberId, Long questionId, Answer answer){
        Member member = memberRepository.findById(memberId).orElse(null);
        Question question = questionRepository.findById(questionId).orElse(null);

        answer.changeMember(member);
        answerRepository.save(answer);

        question.changeAnswer(answer);
        questionRepository.save(question);

        return answer.getId();
    }
}
