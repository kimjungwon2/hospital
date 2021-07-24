package site.hospital.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.member.Member;
import site.hospital.dto.AdminMemberSearchCondition;

public interface MemberRepositoryCustom {
    Page<Member> adminSearchMembers(AdminMemberSearchCondition condition, Pageable pageable);
    Page<Member> adminMembers(Pageable pageable);
}
