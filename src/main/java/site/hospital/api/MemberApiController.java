package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.member.Authorization;
import site.hospital.domain.member.Member;
import site.hospital.dto.AdminMemberSearchCondition;
import site.hospital.service.MemberService;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public LoginMemberResponse loginMember(@RequestBody @Validated LoginMemberRequest request){

        Member member = memberService.logIn(request.getMemberIdName(),request.getPassword());
        LoginMemberResponse loginMemberResponse = new LoginMemberResponse(member);

        return loginMemberResponse;
    }

    @PostMapping("/signup")
    public CreateMemberResponse saveMember(@RequestBody @Validated CreateMemberRequest request){
        Member member= Member.builder()
                .memberIdName(request.getMemberIdName())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .userName(request.getUserName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        Long id = memberService.signUp(member);

        return new CreateMemberResponse(id);
    }

    //관리자 유저 조회
    @GetMapping("/admin/users")
    public Page<MemberSearchResult> adminMembers(Pageable pageable){
        Page<Member> members = memberService.adminMembers(pageable);
        List<MemberSearchResult> result = members.stream()
                .map(m->new MemberSearchResult(m)).collect(Collectors.toList());

        Long total =members.getTotalElements();

        return new PageImpl<>(result, pageable,total);
    }

    //관리자 유저 검색
    @GetMapping("/admin/users/search")
    public Page<MemberSearchResult> adminSearchMembers(@RequestParam(value="allSearch",required = false) String allSearch,
                                                       @RequestParam(value="memberId",required = false) Long memberId,
                                                       @RequestParam(value="memberIdName",required = false) String memberIdName,
                                                       @RequestParam(value="nickName",required = false) String nickName,
                                                       @RequestParam(value="userName",required = false) String userName,
                                                       @RequestParam(value="phoneNumber",required = false) String phoneNumber,
                                                       @RequestParam(value="authorizationStatus",required = false) Authorization authorizationStatus,
                                                       @RequestParam(value="hospitalNumber",required = false) Long hospitalNumber,
                                                       Pageable pageable){

        //받은 값들 생성자로 생성.
        AdminMemberSearchCondition condition = AdminMemberSearchCondition.builder()
                .allSearch(allSearch).memberId(memberId).memberIdName(memberIdName).nickName(nickName)
        .userName(userName).phoneNumber(phoneNumber).authorizationStatus(authorizationStatus).hospitalNumber(hospitalNumber).build();

        Page<Member> members = memberService.adminSearchMembers(condition, pageable);
        List<MemberSearchResult> result = members.stream()
                .map(m->new MemberSearchResult(m)).collect(Collectors.toList());

        Long total =members.getTotalElements();

        return new PageImpl<>(result, pageable,total);
    }

    //관리자 유저 상세 조회
    @GetMapping("/admin/view/member/{memberId}")
    public adminViewMember adminViewMember(@PathVariable("memberId") Long memberId){
        Member member = memberService.viewMember(memberId);
        adminViewMember adminViewMember =new adminViewMember(member);

        return adminViewMember;
    }

    //관리자 멤버 생성
    @PostMapping("/admin/signup")
    public CreateMemberResponse adminSaveMember(@RequestBody @Validated AdminCreateMemberRequest request){
        Member member= Member.builder()
                .memberIdName(request.getMemberIdName())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .userName(request.getUserName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        Long id = memberService.adminSignUp(member, request.getAuthorizationStatus());

        return new CreateMemberResponse(id);
    }

    //관리자 멤버 권한 부여
    @PutMapping("/admin/authorize/member/{memberId}")
    public void adminAuthorize(@PathVariable("memberId") Long memberId, @Validated @RequestBody AdminMemberAuthorizeRequest request){
        memberService.authorize(memberId,request.getAuthorizationStatus());
    }

    //관리자 멤버 삭제
    @DeleteMapping("/admin/delete/member/{memberId}")
    public void adminDeleteMember(@PathVariable("memberId") Long memberId){
        memberService.adminDeleteMember(memberId);
    }

    //관리자 멤버 수정하기
    @PutMapping("/admin/modify/member/{memberId}")
    public void adminModifyMember(@PathVariable("memberId") Long memberId,
                                  @RequestBody @Validated AdminModifyMemberRequest request){
        Member member = Member.builder().phoneNumber(request.getPhoneNumber()).nickName(request.getNickName())
                .userName(request.getUserName()).build();
        memberService.adminModifyMember(memberId, member, request.getAuthorizationStatus());
    }

    /* DTO */
    @Data
    private static class CreateMemberResponse {
        Long memberId;
        public CreateMemberResponse(long memberId){
            this.memberId = memberId;
        }
    }

    @Data
    private static class CreateMemberRequest {
        //회원 이름
        @NotNull(message="이름을 입력해주세요.")
        private String userName;
        //회원 아이디
        @Email(message="올바른 이메일 형태가 아닙니다.")
        @NotBlank(message="공백없이 아이디를 입력해주세요.")
        private String memberIdName;
        @NotNull(message="비밀번호를 입력해주세요.")
        private String password;
        @NotNull(message="닉네임을 입력해주세요.")
        private String nickName;
        @NotNull(message="전화번호를 입력해주세요.")
        private String phoneNumber;
    }

    @Data
    private static class LoginMemberRequest{
        String memberIdName;
        String password;
    }

    @Data
    private static class LoginMemberResponse{
        private Long memberId;
        private String memberIdName;
        private String password;
        private String nickName;
        private String name;
        private String phoneNumber;
        private Authorization authorizationStatus;
        private Long hospitalNumber;

        public LoginMemberResponse(Member member) {
            this.memberId = member.getId();
            this.memberIdName = member.getMemberIdName();
            this.password = member.getPassword();
            this.nickName = member.getNickName();
            this.name = member.getUserName();
            this.phoneNumber = member.getPhoneNumber();
            this.authorizationStatus = member.getAuthorizationStatus();
            this.hospitalNumber = member.getHospitalNumber();
        }
    }

    @Data
    public static class MemberSearchResult {
        private Long memberId;
        private String memberIdName;
        private String nickName;
        private String userName;
        private String phoneNumber;
        private Authorization authorizationStatus;
        private Long hospitalNumber;

        public MemberSearchResult(Member member) {
            this.memberId = member.getId();
            this.memberIdName = member.getMemberIdName();
            this.nickName = member.getNickName();
            this.userName = member.getUserName();
            this.phoneNumber = member.getPhoneNumber();
            this.authorizationStatus = member.getAuthorizationStatus();
            this.hospitalNumber = member.getHospitalNumber();
        }
    }

    @Data
    private static class adminViewMember{
        private Long id;
        private String memberIdName;
        private String userName;
        private String nickName;
        private String phoneNumber;
        private Authorization authorizationStatus;

        public adminViewMember(Member member) {
            this.id = member.getId();
            this.memberIdName = member.getMemberIdName();
            this.userName = member.getUserName();
            this.nickName = member.getNickName();
            this.phoneNumber = member.getPhoneNumber();
            this.authorizationStatus = member.getAuthorizationStatus();
        }
    }
    @Data
    private static class AdminMemberAuthorizeRequest{
        private Authorization authorizationStatus;
    }
    @Data
    private static class AdminModifyMemberRequest{
        private Authorization authorizationStatus;
        private String nickName;
        private String phoneNumber;
        private String userName;
    }

    @Data
    private static class AdminCreateMemberRequest {
        //회원 이름
        @NotNull(message="이름을 입력해주세요.")
        private String userName;
        //회원 아이디
        @Email(message="올바른 이메일 형태가 아닙니다.")
        @NotBlank(message="공백없이 아이디를 입력해주세요.")
        private String memberIdName;
        @NotNull(message="비밀번호를 입력해주세요.")
        private String password;
        @NotNull(message="닉네임을 입력해주세요.")
        private String nickName;
        @NotNull(message="전화번호를 입력해주세요.")
        private String phoneNumber;
        @NotNull(message="권한을 넣어주세요.")
        private Authorization authorizationStatus;
    }
}
