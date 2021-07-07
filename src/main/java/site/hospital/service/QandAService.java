package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Hospital;
import site.hospital.domain.Member;
import site.hospital.domain.QandA;
import site.hospital.repository.HospitalRepository;
import site.hospital.repository.QandARepository;
import site.hospital.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QandAService {

    private final QandARepository qandARepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;

    //QandA 작성
    @Transactional
    public Long qandACreate(Long memberId, Long hospitalId, String content){
        Member member = memberRepository.findById(memberId).orElse(null);
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        QandA qandA = QandA.CreateQandA(member,hospital,content);
        qandARepository.save(qandA);

        return qandA.getId();
    }
}
