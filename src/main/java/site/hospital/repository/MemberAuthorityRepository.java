package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.member.MemberAuthority;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
}
