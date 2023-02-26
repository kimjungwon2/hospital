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

    List<MemberAuthority> findMemberAuthorities(String memberIdName);

    MemberAuthority findManagerAuthority(Long memberId, Authorization authorization);

    void adminDeleteAllAuthority(Member member);
}
