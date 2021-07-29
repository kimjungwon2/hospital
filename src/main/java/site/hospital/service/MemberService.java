package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.member.*;
import site.hospital.dto.AdminMemberSearchCondition;
import site.hospital.repository.AuthorityRepository;
import site.hospital.repository.MemberAuthorityRepository;
import site.hospital.repository.member.MemberRepository;


import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    //회원등록
    @Transactional
    public Long signUp(Member memberDto){
        validateDuplicateMember(memberDto);


        Member member = Member.builder().userName(memberDto.getUserName())
                .nickName(memberDto.getNickName()).phoneNumber(memberDto.getPhoneNumber())
                .memberIdName(memberDto.getMemberIdName()).memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode(memberDto.getPassword())).build();

        //USER 권한 찾기
        Authority authority = authorityRepository.findByAuthorizationStatus(Authorization.ROLE_USER);
        if(authority == null) throw new IllegalStateException("USER 권한 데이터가 없습니다.");

        memberRepository.save(member);

        //새로 생성한 멤버에게 USER 권한 주기.
        MemberAuthority memberAuthority = MemberAuthority.builder()
                .member(member).authority(authority).build();

        memberAuthorityRepository.save(memberAuthority);

        return member.getId();
    }

    public Member logIn(String memberIdName, String password){
        Member findMembers = memberRepository.findOneByMemberIdName(memberIdName);

        if(findMembers == null){
            throw new IllegalStateException("해당 아이디는 존재하지 않습니다.");
        }

        return findMembers;
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

    //관리자 회원등록
    @Transactional
    public Long adminSignUp(Member member){
        validateDuplicateMember(member);

        //USER 권한 주기
        if(member.getMemberStatus() == MemberStatus.NORMAL ||
                member.getMemberStatus() == MemberStatus.STAFF ||
                member.getMemberStatus() == MemberStatus.ADMIN){
            //USER 권한 찾기
            Authority authority_USER = authorityRepository.findByAuthorizationStatus(Authorization.ROLE_USER);
            if(authority_USER == null) throw new IllegalStateException("USER 권한 데이터가 없습니다.");
            memberRepository.save(member);

            //새로 생성한 멤버에게 USER 권한 주기.
            MemberAuthority userAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_USER).build();

            memberAuthorityRepository.save(userAuthority);
        }

        //MANAGER 권한 주기(staff, admin 포함)
        if(member.getMemberStatus() ==MemberStatus.STAFF || member.getMemberStatus() == MemberStatus.ADMIN){
            Authority authority_STAFF = authorityRepository.findByAuthorizationStatus(Authorization.ROLE_MANAGER);
            if (authority_STAFF == null) throw new IllegalStateException("MANAGER 권한 데이터가 없습니다.");

            MemberAuthority managerAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_STAFF).build();

            memberAuthorityRepository.save(managerAuthority);
        }
        //ADMIN 권한 주기(관리자만)
        if(member.getMemberStatus() == MemberStatus.ADMIN){
            Authority authority_ADMIN = authorityRepository.findByAuthorizationStatus(Authorization.ROLE_ADMIN);
            if(authority_ADMIN == null) throw new IllegalStateException("ADMIN 권한 데이터가 없습니다.");

            //admin 권한 저장하기
            MemberAuthority adminAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_ADMIN).build();

            memberAuthorityRepository.save(adminAuthority);
        }

        return member.getId();
    }

    //관리자 멤버 삭제하기
    @Transactional
    public void adminDeleteMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        memberRepository.deleteById(memberId);
    }

    //관리자 멤버 권한주기
    @Transactional
    public void adminGiveAuthority(Long memberId, MemberStatus memberStatus){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        //유효한 권한만 받기.
       if(memberStatus==null) throw new IllegalStateException("권한을 입력하세요");

       MemberAuthority findMemberManager = memberRepository.findMemberStaffAuthority(memberId, Authorization.ROLE_MANAGER);

       //이미 MANAGER 권한이 있으면 MANAGER 권한을 주지 않는다.
       if(findMemberManager == null) {
           Authority authority_STAFF = authorityRepository.findByAuthorizationStatus(Authorization.ROLE_MANAGER);
           if (authority_STAFF == null) throw new IllegalStateException("MANAGER 권한 데이터가 없습니다.");

           //manager 권한 주기
           MemberAuthority managerAuthority = MemberAuthority.builder()
                   .member(member).authority(authority_STAFF).build();
           memberAuthorityRepository.save(managerAuthority);
       }

        //멤버의 staff 권한 주기
       if(memberStatus == MemberStatus.STAFF){
           member.giveAuthority(MemberStatus.STAFF);
       }
        //admin 권한 주기
       else if(memberStatus == MemberStatus.ADMIN){
            Authority authority_ADMIN = authorityRepository.findByAuthorizationStatus(Authorization.ROLE_ADMIN);
            if(authority_ADMIN == null) throw new IllegalStateException("ADMIN 권한 데이터가 없습니다.");

            //admin 권한 저장하기
            MemberAuthority adminAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_ADMIN).build();

            member.giveAuthority(MemberStatus.ADMIN);
            memberAuthorityRepository.save(adminAuthority);
        }

    }
}
