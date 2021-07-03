package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {

}
