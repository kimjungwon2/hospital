package site.hospital.member.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.member.user.domain.MemberAuthority;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {

}
