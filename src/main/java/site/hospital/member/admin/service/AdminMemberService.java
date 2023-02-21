package site.hospital.member.admin.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.member.admin.api.dto.MemberAdminCreateRequest;
import site.hospital.member.admin.api.dto.MemberAdminModifyRequest;
import site.hospital.member.admin.api.dto.MemberAdminViewInfoResponse;
import site.hospital.member.admin.repository.dto.AdminMemberSearchCondition;
import site.hospital.member.user.api.dto.MemberCreateResponse;
import site.hospital.member.user.api.dto.MemberSearchResponse;
import site.hospital.member.user.domain.Authority;
import site.hospital.member.user.domain.Authorization;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberAuthority;
import site.hospital.member.user.domain.MemberStatus;
import site.hospital.member.user.repository.AuthorityRepository;
import site.hospital.member.user.repository.MemberAuthorityRepository;
import site.hospital.member.user.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final HospitalRepository hospitalRepository;
    private final PasswordEncoder passwordEncoder;

    //관리자 멤버 삭제하기
    @Transactional
    public void adminDeleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        //멤버 권리부터 삭제.
        memberRepository.adminDeleteMemberAuthority(member);
        memberRepository.deleteById(memberId);
    }


    //멤버 수정하기
    @Transactional
    public void adminModifyMember(Long memberId, MemberAdminModifyRequest request) {
        Member modifyMember = Member.builder().phoneNumber(request.getPhoneNumber())
                .memberStatus(request.getMemberStatus())
                .nickName(request.getNickName())
                .hospitalNumber(request.getHospitalId())
                .userName(request.getUserName())
                .build();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        //권한이 같다면 권한 수정을 안 한다.
        if (member.getMemberStatus() == modifyMember.getMemberStatus() &&
                modifyMember.getMemberStatus() != MemberStatus.STAFF) {
            member.modifyMember(modifyMember);
        }
        //관리자는 권한 수정을 못 하게 한다.
        else if (modifyMember.getMemberStatus() == MemberStatus.ADMIN) {
            member.modifyMember(modifyMember);
        } else if (modifyMember.getMemberStatus() == MemberStatus.NORMAL) {
            //모든 권한 삭제
            memberRepository.adminDeleteMemberAuthority(member);

            //ROLE_USER 권한 주기
            Authority authority_USER = authorityRepository
                    .findByAuthorizationStatus(Authorization.ROLE_USER);
            if (authority_USER == null) {
                throw new IllegalStateException("USER 권한 데이터가 없습니다.");
            }

            MemberAuthority memberAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_USER).build();

            memberAuthorityRepository.save(memberAuthority);

            //member 데이터 변환
            member.adminModifyMember(modifyMember);

        } else if (modifyMember.getMemberStatus() == MemberStatus.STAFF) {
            //모든 권한 삭제
            memberRepository.adminDeleteMemberAuthority(member);

            //ROLE_USER, ROLE_MANAGER 권한 주기
            Authority authority_USER = authorityRepository
                    .findByAuthorizationStatus(Authorization.ROLE_USER);
            if (authority_USER == null) {
                throw new IllegalStateException("USER 권한 데이터가 없습니다.");
            }

            MemberAuthority memberAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_USER).build();

            memberAuthorityRepository.save(memberAuthority);

            //병원 있는지 여부 찾기
            Hospital hospital = hospitalRepository.findById(modifyMember.getHospitalNumber())
                    .orElseThrow(() -> new IllegalStateException("해당 번호에 속하는 병원이 존재하지 않습니다."));

            Authority authority_STAFF = authorityRepository
                    .findByAuthorizationStatus(Authorization.ROLE_MANAGER);
            if (authority_STAFF == null) {
                throw new IllegalStateException("MANAGER 권한 데이터가 없습니다.");
            }

            MemberAuthority managerAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_STAFF)
                    .hospitalNo(modifyMember.getHospitalNumber()).build();

            memberAuthorityRepository.save(managerAuthority);

            //member 데이터 변환
            member.adminModifyMember(modifyMember);
        }

    }


    //관리자 멤버 검색
    public Page<MemberSearchResponse> adminSearchMembers(
            String allSearch,
            Long memberId,
            String memberIdName,
            String nickName,
            String userName,
            String phoneNumber,
            MemberStatus memberStatus,
            Long hospitalNumber,
            Pageable pageable
    ) {
        AdminMemberSearchCondition condition =
                AdminMemberSearchCondition
                        .builder()
                        .allSearch(allSearch)
                        .memberId(memberId)
                        .memberIdName(memberIdName)
                        .nickName(nickName)
                        .userName(userName)
                        .phoneNumber(phoneNumber)
                        .memberStatus(memberStatus)
                        .hospitalNumber(hospitalNumber)
                        .build();

        Page<Member> members = memberRepository.adminSearchMembers(condition, pageable);

        List<MemberSearchResponse> result =
                members.stream()
                        .map(m -> MemberSearchResponse.from(m))
                        .collect(Collectors.toList());

        Long total = members.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

    //관리자 회원등록
    @Transactional
    public MemberCreateResponse adminSignUp(MemberAdminCreateRequest request) {
        Member memberDto = Member.builder()
                .memberIdName(request.getMemberIdName())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .userName(request.getUserName())
                .memberStatus(request.getMemberStatus())
                .phoneNumber(request.getPhoneNumber())
                .hospitalNumber(request.getHospitalId())
                .build();

        validateDuplicateMember(memberDto);

        Member member = Member.builder().userName(memberDto.getUserName())
                .hospitalNumber(memberDto.getHospitalNumber())
                .nickName(memberDto.getNickName()).phoneNumber(memberDto.getPhoneNumber())
                .memberIdName(memberDto.getMemberIdName()).memberStatus(memberDto.getMemberStatus())
                .password(passwordEncoder.encode(memberDto.getPassword())).build();

        //USER 권한 주기
        if (member.getMemberStatus() == MemberStatus.NORMAL ||
                member.getMemberStatus() == MemberStatus.STAFF ||
                member.getMemberStatus() == MemberStatus.ADMIN) {
            //USER 권한 찾기
            Authority authority_USER = authorityRepository
                    .findByAuthorizationStatus(Authorization.ROLE_USER);
            if (authority_USER == null) {
                throw new IllegalStateException("USER 권한 데이터가 없습니다.");
            }
            memberRepository.save(member);

            //새로 생성한 멤버에게 USER 권한 주기.
            MemberAuthority userAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_USER).build();

            memberAuthorityRepository.save(userAuthority);
        }

        //MANAGER 권한 주기(staff)
        if (member.getMemberStatus() == MemberStatus.STAFF) {

            //병원 번호 있는지 여부 찾기
            Hospital hospital = hospitalRepository.findById(member.getHospitalNumber())
                    .orElseThrow(() -> new IllegalStateException("해당 번호에 속하는 병원이 존재하지 않습니다."));

            Authority authority_STAFF = authorityRepository
                    .findByAuthorizationStatus(Authorization.ROLE_MANAGER);
            if (authority_STAFF == null) {
                throw new IllegalStateException("MANAGER 권한 데이터가 없습니다.");
            }

            MemberAuthority managerAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_STAFF)
                    .hospitalNo(member.getHospitalNumber()).build();

            memberAuthorityRepository.save(managerAuthority);
        }
        //MANAGER 권한 주기(관리자만)
        if (member.getMemberStatus() == MemberStatus.ADMIN) {
            Authority authority_STAFF = authorityRepository
                    .findByAuthorizationStatus(Authorization.ROLE_MANAGER);
            if (authority_STAFF == null) {
                throw new IllegalStateException("MANAGER 권한 데이터가 없습니다.");
            }

            MemberAuthority managerAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_STAFF).build();

            memberAuthorityRepository.save(managerAuthority);
        }

        //ADMIN 권한 주기(관리자만)
        if (member.getMemberStatus() == MemberStatus.ADMIN) {
            Authority authority_ADMIN = authorityRepository
                    .findByAuthorizationStatus(Authorization.ROLE_ADMIN);
            if (authority_ADMIN == null) {
                throw new IllegalStateException("ADMIN 권한 데이터가 없습니다.");
            }

            //admin 권한 저장하기
            MemberAuthority adminAuthority = MemberAuthority.builder()
                    .member(member).authority(authority_ADMIN).build();

            memberAuthorityRepository.save(adminAuthority);
        }

        return MemberCreateResponse.from(member.getId());
    }


    //관리자 멤버 상세정보 보기
    public MemberAdminViewInfoResponse adminViewMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

        return MemberAdminViewInfoResponse.from(member);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByMemberIdName(member.getMemberIdName());

        if (findMembers!=null && !findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원.");
        }
    }

}
