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
import site.hospital.member.user.service.MemberService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final HospitalRepository hospitalRepository;
    private final PasswordEncoder passwordEncoder;

    private final static String MEMBER_NOT_EXISTS = "멤버가 존재하지 않습니다.";

    @Transactional
    @Override
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException(MEMBER_NOT_EXISTS));

        memberRepository.adminDeleteAllAuthority(member);
        memberRepository.deleteById(memberId);
    }

    @Transactional
    @Override
    public void modifyMember(Long memberId, MemberAdminModifyRequest request) {
        Member memberChange = Member
                .builder()
                .phoneNumber(request.getPhoneNumber())
                .memberStatus(request.getMemberStatus())
                .nickName(request.getNickName())
                .hospitalNumber(request.getHospitalId())
                .userName(request.getUserName())
                .build();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException(MEMBER_NOT_EXISTS));

        if (confirmSameAuthority(memberChange, member)) {
            member.modifyMember(memberChange);
        } else if (confirmAdminAuthority(memberChange)) {
            member.modifyMember(memberChange);
        } else if (confirmUserAuthority(memberChange)) {
            memberRepository.adminDeleteAllAuthority(member);

            giveUserAuthority(member);
            member.adminModifyMember(memberChange);
        } else if (confirmManagerAuthority(memberChange)) {
            memberRepository.adminDeleteAllAuthority(member);

            giveUserAuthority(member);
            confirmHospitalPresence(memberChange);
            giveManagerAuthority(memberChange, member);

            member.adminModifyMember(memberChange);
        }

    }

    @Override
    public Page<MemberSearchResponse> searchMembers(
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
        AdminMemberSearchCondition searchCondition =
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

        Page<Member> searchedMembers = memberRepository.adminSearchMembers(searchCondition, pageable);

        return getPagingMember(pageable, searchedMembers);
    }

    @Transactional
    @Override
    public MemberCreateResponse signup(MemberAdminCreateRequest request) {
        Member createdMember = Member
                .builder()
                .memberIdName(request.getMemberIdName())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickName(request.getNickName())
                .userName(request.getUserName())
                .memberStatus(request.getMemberStatus())
                .phoneNumber(request.getPhoneNumber())
                .hospitalNumber(request.getHospitalId())
                .build();

        memberService.validateDuplicateMember(createdMember);

        if (confirmUserAuthority(createdMember) ||
                confirmManagerAuthority(createdMember) ||
                confirmAdminAuthority(createdMember)
        ) {
            memberRepository.save(createdMember);
            giveUserAuthority(createdMember);
        }

        if (confirmManagerAuthority(createdMember)){
            confirmHospitalPresence(createdMember);
            giveManagerAuthority(createdMember);
        }
        else if (confirmAdminAuthority(createdMember)) {
            giveManagerAuthorityByAdmin(createdMember);
        }

        if (confirmAdminAuthority(createdMember)) {
            giveAdminAuthority(createdMember);
        }

        return MemberCreateResponse.from(createdMember.getId());
    }

    @Override
    public MemberAdminViewInfoResponse viewMemberInformation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException(MEMBER_NOT_EXISTS));

        return MemberAdminViewInfoResponse.from(member);
    }

    private void giveAdminAuthority(Member createdMember) {
        Authority authority_ADMIN = findAdminAuthority();

        MemberAuthority adminAuthority = MemberAuthority.builder()
                .member(createdMember).authority(authority_ADMIN).build();

        memberAuthorityRepository.save(adminAuthority);
    }

    private Authority findAdminAuthority() {
        Authority authority_ADMIN = authorityRepository
                .findByAuthorizationStatus(Authorization.ROLE_ADMIN);

        if (authority_ADMIN == null) {
            throw new IllegalStateException("ADMIN 권한 데이터가 없습니다.");
        }
        return authority_ADMIN;
    }

    private void giveManagerAuthorityByAdmin(Member createdMember) {
        Authority authority_MANAGER = findManagerAuthority();

        MemberAuthority managerAuthority = MemberAuthority
                .builder()
                .member(createdMember)
                .authority(authority_MANAGER)
                .build();

        memberAuthorityRepository.save(managerAuthority);
    }


    private void giveManagerAuthority(Member modifyMember, Member member) {
        Authority authority_MANAGER = findManagerAuthority();

        MemberAuthority managerAuthority = MemberAuthority
                .builder()
                .member(member)
                .authority(authority_MANAGER)
                .hospitalNo(modifyMember.getHospitalNumber())
                .build();

        memberAuthorityRepository.save(managerAuthority);
    }

    private void giveManagerAuthority(Member member) {
        Authority authority_MANAGER = findManagerAuthority();

        MemberAuthority managerAuthority = MemberAuthority
                .builder()
                .member(member)
                .authority(authority_MANAGER)
                .hospitalNo(member.getHospitalNumber())
                .build();

        memberAuthorityRepository.save(managerAuthority);
    }

    private Authority findManagerAuthority() {
        Authority authority_MANAGER = authorityRepository
                .findByAuthorizationStatus(Authorization.ROLE_MANAGER);

        if (authority_MANAGER == null) {
            throw new IllegalStateException("MANAGER 권한 데이터가 없습니다.");
        }
        return authority_MANAGER;
    }

    private void confirmHospitalPresence(Member member) {
        hospitalRepository.findById(member.getHospitalNumber())
                .orElseThrow(() -> new IllegalStateException("해당 번호에 속하는 병원이 존재하지 않습니다."));
    }

    private boolean confirmManagerAuthority(Member modifyMember) {
        return modifyMember.getMemberStatus() == MemberStatus.STAFF;
    }

    private void giveUserAuthority(Member member) {
        Authority authority_USER = findUserAuthority();

        MemberAuthority memberAuthority = MemberAuthority
                .builder()
                .member(member)
                .authority(authority_USER)
                .build();

        memberAuthorityRepository.save(memberAuthority);
    }

    private Authority findUserAuthority() {
        Authority authority_USER = authorityRepository
                .findByAuthorizationStatus(Authorization.ROLE_USER);

        if (authority_USER == null) {
            throw new IllegalStateException("USER 권한 데이터가 없습니다.");
        }
        return authority_USER;
    }

    private boolean confirmUserAuthority(Member modifyMember) {
        return modifyMember.getMemberStatus() == MemberStatus.NORMAL;
    }

    private boolean confirmAdminAuthority(Member modifyMember) {
        return modifyMember.getMemberStatus() == MemberStatus.ADMIN;
    }

    private boolean confirmSameAuthority(Member modifyMember, Member member) {
        return member.getMemberStatus() == modifyMember.getMemberStatus() &&
                modifyMember.getMemberStatus() != MemberStatus.STAFF;
    }

    private PageImpl getPagingMember(Pageable pageable, Page<Member> searchedMembers) {
        List<MemberSearchResponse> result = searchedMembers
                .stream()
                .map(m -> MemberSearchResponse.from(m))
                .collect(Collectors.toList());

        Long total = searchedMembers.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

}
