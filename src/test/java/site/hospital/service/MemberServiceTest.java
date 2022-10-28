package site.hospital.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.member.Authority;
import site.hospital.domain.member.Authorization;
import site.hospital.domain.member.Member;
import site.hospital.domain.member.MemberStatus;
import site.hospital.repository.AuthorityRepository;
import site.hospital.repository.member.MemberRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    MemberService memberService;

    @BeforeEach
    public void 권한생성(){
        Authority authority_user = new Authority(Authorization.ROLE_USER);
        authorityRepository.save(authority_user);

        Authority authority_manager = new Authority(Authorization.ROLE_MANAGER);
        authorityRepository.save(authority_manager);

        Authority authority_admin = new Authority(Authorization.ROLE_ADMIN);
        authorityRepository.save(authority_admin);
    }

    @Test
    @Rollback
    public void 회원가입() {
        //given
        Member member = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com").memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode("1234")).build();

        //when
        Long id = memberService.signUp(member);

        Optional<Member> findMember = memberRepository.findById(id);

        //then
        assertEquals(member.getMemberIdName(),findMember.get().getMemberIdName());
        assertEquals(member.getNickName(),findMember.get().getNickName());
        assertEquals(member.getUserName(),findMember.get().getUserName());
        assertEquals(member.getPhoneNumber(),findMember.get().getPhoneNumber());
        assertEquals(member.getMemberStatus(),findMember.get().getMemberStatus());
    }

}