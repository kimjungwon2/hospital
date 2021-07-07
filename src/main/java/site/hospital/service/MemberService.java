package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Member;
import site.hospital.repository.member.simplequery.MemberSearchCondition;
import site.hospital.repository.member.simplequery.MemberSearchResult;
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


    //멤버 Search
    public Page<MemberSearchResult> search(MemberSearchCondition condition, Pageable pageable){
        return memberRepository.search(condition, pageable);
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByMemberIdName(member.getMemberIdName());

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원.");
        }
    }


}
