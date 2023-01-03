package site.hospital.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.member.MemberAuthority;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {

    List<MemberAuthority> findByMemberId(Long memberId);
}
