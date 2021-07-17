package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.domain.QandA;
import site.hospital.repository.hospital.HospitalRepository;
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

    //QandA 수정
    @Transactional
    public Long modifyQandA(Long qanda_id, String content){
        QandA qandA = qandARepository.findById(qanda_id).orElse(null);
        qandA.modifyQandA(content);

        return qandA.getId();
    }

    //QandA 삭제
    @Transactional
    public void deleteQandA(Long id){
        qandARepository.deleteById(id);
    }
}
