package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.member.Authorization;
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

    //멤버 수정하기
    @Transactional
    public void modifyMember(Long memberId, Member modifyMember){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        member.modifyMember(modifyMember);
    }

    //관리자 멤버 보기
    public Page<Member> adminMembers(Pageable pageable){
        return memberRepository.adminMembers(pageable);
    }


    public Page<Member> adminSearchMembers(AdminMemberSearchCondition condition, Pageable pageable){
        return memberRepository.adminSearchMembers(condition,pageable);
    }

    //멤버 상세정보 보기
    public Member viewMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        return member;
    }

    //회원등록
    @Transactional
    public Long adminSignUp(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }



    //관리자 멤버 삭제하기
    @Transactional
    public void adminDeleteMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        memberRepository.deleteById(memberId);
    }



}
