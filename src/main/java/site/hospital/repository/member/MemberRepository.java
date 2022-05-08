package site.hospital.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface    MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {
    //회원 이름 찾기
    List<Member> findByMemberIdName(String MemberIdName);

    //로그인 하기
    Member findOneByMemberIdName(String memberIdName);

    //유저 이메일 찾기
    Optional<Member> findOneEmailByMemberIdName(String memberIdName);

}
