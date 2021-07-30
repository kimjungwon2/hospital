package site.hospital.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.member.Authorization;
import site.hospital.domain.member.Member;
import site.hospital.domain.member.MemberAuthority;
import site.hospital.dto.AdminMemberSearchCondition;

import java.util.List;

public interface MemberRepositoryCustom {
    Page<Member> adminSearchMembers(AdminMemberSearchCondition condition, Pageable pageable);
    Page<Member> adminMembers(Pageable pageable);
    List<MemberAuthority> memberAuthorities(String memberIdName);
    MemberAuthority findMemberStaffAuthority(Long memberId, Authorization authorization);
    void adminDeleteMemberAuthority(Member member);
}
