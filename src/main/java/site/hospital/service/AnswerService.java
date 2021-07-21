package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Answer;
import site.hospital.domain.QandA;
import site.hospital.domain.member.Member;
import site.hospital.repository.AnswerRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.qandA.QandARepository;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class AnswerService {
    private final MemberRepository memberRepository;
    private final QandARepository qandARepository;
    private final AnswerRepository answerRepository;

    //답변 등록
    @Transactional
    public Long registerAnswer(Long memberId, Long qandAId, Answer answer){
        Member member = memberRepository.findById(memberId).orElse(null);
        QandA qandA = qandARepository.findById(qandAId).orElse(null);

        answer.createAnswer(member,qandA);
        answerRepository.save(answer);

        return answer.getId();
    }
}
