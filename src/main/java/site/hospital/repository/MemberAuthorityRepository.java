package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.member.MemberAuthority;
import java.util.*;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
    List<MemberAuthority> findByMemberId(Long memberId);
}
