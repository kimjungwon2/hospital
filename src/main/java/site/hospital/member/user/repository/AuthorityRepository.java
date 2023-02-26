package site.hospital.member.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.member.user.domain.Authority;
import site.hospital.member.user.domain.Authorization;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByAuthorizationStatus(Authorization authorization);
}
