package site.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Test
    public void 중복_회원가입(){
        //given
        Member member1 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com").memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode("1234")).build();


        Member member2 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com").memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode("1234")).build();

        //when
        memberService.signUp(member1);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.signUp(member2);
        });
    }

    @Test
    public void 회원가입_null체크(){
        //given
        Member member1 = Member.builder().userName(null)
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com").memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode("1234")).build();

        Member member2 = Member.builder().userName("이덕후")
                .nickName(null).phoneNumber("01057469163")
                .memberIdName("a@naver2.com").memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode("1234")).build();

        Member member3 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber(null)
                .memberIdName("a@naver2.com").memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode("1234")).build();

        Member member4 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName(null).memberStatus(MemberStatus.NORMAL)
                .password(passwordEncoder.encode("1234")).build();

        Member member5 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com").memberStatus(null)
                .password(passwordEncoder.encode("1234")).build();


        //then
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            memberService.signUp(member1);
        });

        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            memberService.signUp(member2);
        });

        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            memberService.signUp(member3);
        });

        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            memberService.signUp(member4);
        });


        assertThrows(IllegalArgumentException.class, () -> {
            Member.builder().userName("이덕후")
                    .nickName("풉키풉키").phoneNumber("01057469163")
                    .memberIdName("a@naver2.com").memberStatus(MemberStatus.NORMAL)
                    .password(passwordEncoder.encode(null)).build();
        });
    }

    //회원가입 권한 설정.(FE에서 임의로 조정해도 정상적으로 NORMAL로 되는지 확인)
    @Test
    public void 회원가입_권한() {
        //given
        Member member1 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com").memberStatus(MemberStatus.ADMIN)
                .password(passwordEncoder.encode("1234")).build();

        Member member2 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver3.com").memberStatus(MemberStatus.STAFF)
                .password(passwordEncoder.encode("1234")).build();

        //when
        Long id1 = memberService.signUp(member1);
        Long id2 = memberService.signUp(member2);

        Optional<Member> findMember1 = memberRepository.findById(id1);
        Optional<Member> findMember2 = memberRepository.findById(id2);

        //then
        assertEquals(MemberStatus.NORMAL,findMember1.get().getMemberStatus());
        assertEquals(MemberStatus.NORMAL,findMember2.get().getMemberStatus());
    }


}