package site.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.hospital.api.HospitalApiController;
import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.domain.hospital.Hospital;
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

    @Autowired
    HospitalService hospitalService;

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

    @Test
    public void 회원정보_수정(){
        //given
        Member member1 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com")
                .password(passwordEncoder.encode("1234")).build();
        Long id1 = memberService.signUp(member1);

        //when
        Member modifyMember = Member.builder().phoneNumber("01056135554").nickName("화유")
                .userName("김희철").memberStatus(MemberStatus.ADMIN).build();

        memberService.modifyMember(id1,modifyMember);

        Optional<Member> findMember = memberRepository.findById(id1);

        //then
        assertEquals(findMember.get().getPhoneNumber(),"01056135554");
        assertEquals(findMember.get().getNickName(),"화유");
        assertEquals(findMember.get().getUserName(),"김희철");
        assertEquals(findMember.get().getMemberStatus(),MemberStatus.NORMAL);
    }

    @Test
    public void 회원이없는정보_수정(){
        //given
        Member member1 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com")
                .password(passwordEncoder.encode("1234")).build();
        Long id1 = memberService.signUp(member1);

        //when
        Member modifyMember = Member.builder().phoneNumber("01056135554").nickName("화유")
                .userName("김희철").memberStatus(MemberStatus.ADMIN).build();

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.modifyMember(2L,modifyMember);
        });
    }

    @Test
    public void 관리자_회원정보_수정(){
        //given
        Member member1 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com")
                .password(passwordEncoder.encode("1234")).build();
        Long id1 = memberService.signUp(member1);

        Member member2 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver3.com")
                .password(passwordEncoder.encode("1234")).build();
        Long id2 = memberService.signUp(member2);

        //병원 생성(병원 번호를 얻기 위해)
        Hospital hospital = Hospital.builder()
                .licensingDate("20121008")
                .hospitalName("가평산속요양병원")
                .phoneNumber("031-584-8900")
                .distinguishedName("요양병원(일반요양병원)")
                .medicalSubjectInformation("내과, 한방내과")
                .businessCondition(BusinessCondition.영업중)
                .cityName("가평군")
                .build();

        hospitalService.registerHospital(hospital);

        //when
        Member modifyMember1 = Member.builder().phoneNumber("01056135554")
                .memberStatus(MemberStatus.ADMIN)
                .nickName("화유")
                .hospitalNumber(2L)
                .userName("김희철").build();

        Member modifyMember2 = Member.builder().phoneNumber("01064311551")
                .memberStatus(MemberStatus.STAFF)
                .nickName("유대")
                .hospitalNumber(1L)
                .userName("김희도").build();


        memberService.adminModifyMember(id1,modifyMember1);

        memberService.adminModifyMember(id2,modifyMember2);

        Optional<Member> findMember = memberRepository.findById(id1);
        Optional<Member> findMember2 = memberRepository.findById(id2);

        //then
        assertEquals(findMember.get().getPhoneNumber(),"01056135554");
        assertEquals(findMember.get().getNickName(),"화유");
        assertEquals(findMember.get().getUserName(),"김희철");
        assertEquals(findMember.get().getMemberStatus(),MemberStatus.ADMIN);
        assertEquals(findMember.get().getHospitalNumber(),null);

        assertEquals(findMember2.get().getPhoneNumber(),"01064311551");
        assertEquals(findMember2.get().getNickName(),"유대");
        assertEquals(findMember2.get().getUserName(),"김희도");
        assertEquals(findMember2.get().getMemberStatus(),MemberStatus.STAFF);
        assertEquals(findMember2.get().getHospitalNumber(),1L);
    }

}