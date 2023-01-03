package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.member.Authority;
import site.hospital.domain.member.Authorization;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthorizationStatus(Authorization authorization);
}
