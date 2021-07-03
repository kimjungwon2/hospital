package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Member;
import site.hospital.repository.repository;


import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class MemberService {

    private final repository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicdateMember(member);
        memberRepository.save(member);
        return member.getId(); // 키 값 리턴
    }

    //회원 중복 확인
    private void validateDuplicdateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getUserName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
