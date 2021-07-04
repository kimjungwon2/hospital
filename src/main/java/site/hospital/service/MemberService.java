package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Member;
import site.hospital.dto.MemberSearchCondition;
import site.hospital.dto.MemberSearchResult;
import site.hospital.repository.MemberRepository;


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

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByMemberIdName(member.getMemberIdName());

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원.");
        }
    }

    //멤버 Search
    public Page<MemberSearchResult> search(MemberSearchCondition condition, Pageable pageable){
        return memberRepository.search(condition, pageable);
    }

}
