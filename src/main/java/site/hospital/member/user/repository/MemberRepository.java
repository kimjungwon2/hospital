package site.hospital.member.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.member.user.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByMemberIdName(String MemberIdName);

    Optional<Member> findOneEmailByMemberIdName(String memberIdName);
}
