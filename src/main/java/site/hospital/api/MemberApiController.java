package site.hospital.api;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.member.Authorization;
import site.hospital.domain.member.Member;
import site.hospital.repository.member.simplequery.MemberSearchCondition;
import site.hospital.repository.member.simplequery.MemberSearchResult;
import site.hospital.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/member/login")
    public LoginMemberResponse loginMember(@RequestBody @Validated LoginMemberRequest request){

        Member member = memberService.logIn(request.getMemberIdName(),request.getPassword());
        LoginMemberResponse loginMemberResponse = new LoginMemberResponse(member);

        return loginMemberResponse;
    }



    @PostMapping("/member/signup")
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

    @GetMapping("/member/search")
    public Page<MemberSearchResult> searchMember(MemberSearchCondition condition, Pageable pageable){
        return memberService.search(condition, pageable);
    }


    /* DTO */
    @Data
    private static class CreateMemberResponse {
        long memberId;
        public CreateMemberResponse(long memberId){
            this.memberId = memberId;
        }
    }

    @Data
    private static class CreateMemberRequest {
        //회원 이름
        private String userName;
        //회원 아이디
        private String memberIdName;
        private String password;
        private String nickName;
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

}
