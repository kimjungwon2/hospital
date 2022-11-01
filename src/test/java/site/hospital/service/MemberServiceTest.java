package site.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.*;
import site.hospital.repository.AuthorityRepository;
import site.hospital.repository.MemberAuthorityRepository;
import site.hospital.repository.member.MemberRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberAuthorityRepository memberAuthorityRepository;

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
        assertEquals(findMember.get().getMemberStatus(),MemberStatus.NORMAL);
        assertEquals(findMember.get().getHospitalNumber(),null);

        assertEquals(findMember2.get().getPhoneNumber(),"01064311551");
        assertEquals(findMember2.get().getNickName(),"유대");
        assertEquals(findMember2.get().getUserName(),"김희도");
        assertEquals(findMember2.get().getMemberStatus(),MemberStatus.STAFF);
        assertEquals(findMember2.get().getHospitalNumber(),1L);
    }


    @Test
    public void 관리자_회원정보_수정_권한확인(){
        //given
        Member member1 = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver2.com")
                .password(passwordEncoder.encode("1234")).build();

        Long id1 = memberService.signUp(member1);

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
                .memberStatus(MemberStatus.STAFF)
                .nickName("화유")
                .hospitalNumber(1L)
                .userName("김희철").build();


        memberService.adminModifyMember(id1,modifyMember1);

        List<MemberAuthority> findMemberAuthority = memberAuthorityRepository.findByMemberId(id1);

        //then
        assertEquals(2,findMemberAuthority.size());
        assertEquals(0,findMemberAuthority.get(0).getHospitalNo());
        assertEquals(1,findMemberAuthority.get(0).getAuthority().getId());
        assertEquals(1L,findMemberAuthority.get(1).getHospitalNo());
        assertEquals(2,findMemberAuthority.get(1).getAuthority().getId());
    }


    @Test
    public void 관리자_회원등록(){
        //given
        Member member_normal = Member.builder().userName("이덕후")
                .nickName("풉키풉키").phoneNumber("01057469163")
                .memberIdName("a@naver.com")
                .password(passwordEncoder.encode("1234"))
                .memberStatus(MemberStatus.NORMAL)
                .hospitalNumber(1L).build();

        Member member_staff = Member.builder().userName("이희영")
                .nickName("구름").phoneNumber("01057469163")
                .memberIdName("aa@naver.com")
                .password(passwordEncoder.encode("1234"))
                .memberStatus(MemberStatus.STAFF)
                .hospitalNumber(1L).build();

        Member member_admin = Member.builder().userName("김정일")
                .nickName("릴스타").phoneNumber("01057469163")
                .memberIdName("aaa@naver.com")
                .password(passwordEncoder.encode("1234"))
                .memberStatus(MemberStatus.ADMIN)
                .hospitalNumber(1L).build();

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
        Long noraml_id = memberService.adminSignUp(member_normal);
        Long staff_id = memberService.adminSignUp(member_staff);
        Long admin_id = memberService.adminSignUp(member_admin);


        List<MemberAuthority> findNormalAuthority = memberAuthorityRepository.findByMemberId(noraml_id);
        List<MemberAuthority> findStaffAuthority = memberAuthorityRepository.findByMemberId(staff_id);
        List<MemberAuthority> findAdminAuthority = memberAuthorityRepository.findByMemberId(admin_id);

        Optional<Member> findNormalMember = memberRepository.findById(noraml_id);
        Optional<Member> findStaffMember = memberRepository.findById(staff_id);
        Optional<Member> findAdminMember = memberRepository.findById(admin_id);


        //then

        //Normal 계정 확인
        assertEquals(findNormalMember.get().getPhoneNumber(),"01057469163");
        assertEquals(findNormalMember.get().getMemberIdName(),"a@naver.com");
        assertEquals(findNormalMember.get().getNickName(),"풉키풉키");
        assertEquals(findNormalMember.get().getUserName(),"이덕후");
        assertEquals(findNormalMember.get().getMemberStatus(),MemberStatus.NORMAL);

        assertEquals(1,findNormalAuthority.size());
        assertEquals(0,findNormalAuthority.get(0).getHospitalNo());

        //STAFF 계정 확인
        assertEquals(findStaffMember.get().getPhoneNumber(),"01057469163");
        assertEquals(findStaffMember.get().getMemberIdName(),"aa@naver.com");
        assertEquals(findStaffMember.get().getNickName(),"구름");
        assertEquals(findStaffMember.get().getUserName(),"이희영");
        assertEquals(findStaffMember.get().getMemberStatus(),MemberStatus.STAFF);

        assertEquals(2,findStaffAuthority.size());
        assertEquals(0,findStaffAuthority.get(0).getHospitalNo());
        assertEquals(1,findStaffAuthority.get(1).getHospitalNo());

        //Admin 계정 확인
        assertEquals(findAdminMember.get().getPhoneNumber(),"01057469163");
        assertEquals(findAdminMember.get().getMemberIdName(),"aaa@naver.com");
        assertEquals(findAdminMember.get().getNickName(),"릴스타");
        assertEquals(findAdminMember.get().getUserName(),"김정일");
        assertEquals(findAdminMember.get().getMemberStatus(),MemberStatus.ADMIN);

        assertEquals(3,findAdminAuthority.size());
        assertEquals(0,findAdminAuthority.get(0).getHospitalNo());
        assertEquals(0,findAdminAuthority.get(1).getHospitalNo());
        assertEquals(0,findAdminAuthority.get(2).getHospitalNo());
    }

    @Test
    public void 관리자_admin에서normal_관리자계정수정(){
        //given
        Member member_admin = Member.builder().userName("김정일")
                .nickName("릴스타").phoneNumber("01057469163")
                .memberIdName("aaa@naver.com")
                .password(passwordEncoder.encode("1234"))
                .memberStatus(MemberStatus.ADMIN)
                .hospitalNumber(1L).build();

        Long admin_id = memberService.adminSignUp(member_admin);

        //when
        Member modifyMember1 = Member.builder().phoneNumber("01064311551")
                .memberStatus(MemberStatus.NORMAL)
                .nickName("유대")
                .hospitalNumber(1L)
                .userName("김희도").build();

        memberService.adminModifyMember(admin_id,modifyMember1);

        Optional<Member> findModifyMember1 = memberRepository.findById(admin_id);

        List<MemberAuthority> findMemberAuthority = memberAuthorityRepository.findByMemberId(admin_id);

        //then
        assertEquals(findModifyMember1.get().getPhoneNumber(),"01064311551");
        assertEquals(findModifyMember1.get().getNickName(),"유대");
        assertEquals(findModifyMember1.get().getUserName(),"김희도");
        assertEquals(findModifyMember1.get().getMemberStatus(),MemberStatus.NORMAL);
        assertEquals(findModifyMember1.get().getHospitalNumber(),null);

        assertEquals(1,findMemberAuthority.size());
        assertEquals(0,findMemberAuthority.get(0).getHospitalNo());

    }

    @Test
    public void 관리자_admin에서staff_관리자계정수정(){
        //given
        Member member_admin = Member.builder().userName("김정일")
                .nickName("릴스타").phoneNumber("01057469163")
                .memberIdName("aaa@naver.com")
                .password(passwordEncoder.encode("1234"))
                .memberStatus(MemberStatus.ADMIN)
                .hospitalNumber(1L).build();

        Long admin_id = memberService.adminSignUp(member_admin);

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
        Member modifyMember = Member.builder().phoneNumber("01064311551")
                .memberStatus(MemberStatus.STAFF)
                .nickName("유대")
                .hospitalNumber(1L)
                .userName("김희도").build();

        memberService.adminModifyMember(admin_id,modifyMember);

        Optional<Member> findModifyMember1 = memberRepository.findById(admin_id);

        List<MemberAuthority> findMemberAuthority = memberAuthorityRepository.findByMemberId(admin_id);

        //then
        assertEquals(findModifyMember1.get().getPhoneNumber(),"01064311551");
        assertEquals(findModifyMember1.get().getNickName(),"유대");
        assertEquals(findModifyMember1.get().getUserName(),"김희도");
        assertEquals(findModifyMember1.get().getMemberStatus(),MemberStatus.STAFF);
        assertEquals(findModifyMember1.get().getHospitalNumber(),1L);

        assertEquals(2,findMemberAuthority.size());
        assertEquals(0,findMemberAuthority.get(0).getHospitalNo());
        assertEquals(1,findMemberAuthority.get(1).getHospitalNo());
    }

}