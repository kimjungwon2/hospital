package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.member.Member;
import site.hospital.dto.AdminMemberSearchCondition;
import site.hospital.repository.member.MemberRepository;


import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원등록
    @Transactional
    public Long signUp(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public Member logIn(String memberIdName, String password){
        Member findMembers = memberRepository.findOneByMemberIdName(memberIdName);

        if(findMembers == null){
            throw new IllegalStateException("해당 아이디는 존재하지 않습니다.");
        }

        if(findMembers.getPassword().equals(password)) return findMembers;
        else throw new IllegalStateException("아이디 혹은 비밀번호가 틀렸습니다.");

    }

    //중복 아이디 검사
    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByMemberIdName(member.getMemberIdName());

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원.");
        }
    }

    //관리자 멤버 보기
    public Page<Member> adminMembers(Pageable pageable){
        return memberRepository.adminMembers(pageable);
    }


    public Page<Member> adminSearchMembers(AdminMemberSearchCondition condition, Pageable pageable){
        return memberRepository.adminSearchMembers(condition,pageable);
    }

    // 관리자 멤버 권한 주기
    @Transactional
    public void authorize(Long memberId, String status){
        Member member = memberRepository.findById(memberId).orElse(null);

        member.authorize(status);
    }


}
