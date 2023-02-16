package site.hospital.member.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.member.user.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    //회원 이름 찾기
    List<Member> findByMemberIdName(String MemberIdName);

    //로그인 하기
    Member findOneByMemberIdName(String memberIdName);

    //유저 이메일 찾기
    Optional<Member> findOneEmailByMemberIdName(String memberIdName);

}
