package site.hospital.member.user.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.member.user.domain.Authorization;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberAuthority;
import site.hospital.member.admin.repository.dto.AdminMemberSearchCondition;

public interface MemberRepositoryCustom {

    Page<Member> adminSearchMembers(AdminMemberSearchCondition condition, Pageable pageable);

    List<MemberAuthority> memberAuthorities(String memberIdName);

    MemberAuthority findMemberStaffAuthority(Long memberId, Authorization authorization);

    void adminDeleteMemberAuthority(Member member);
}
