package site.hospital.member.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.member.user.api.dto.MemberCreateRequest;
import site.hospital.member.user.api.dto.MemberCreateResponse;
import site.hospital.member.user.api.dto.MemberLoginRequest;
import site.hospital.member.user.api.dto.MemberLoginResponse;
import site.hospital.member.user.api.dto.MemberModifyRequest;
import site.hospital.member.user.api.dto.MemberViewInfoResponse;
import site.hospital.member.user.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> loginMember(
            @RequestBody @Validated MemberLoginRequest request) {
        return memberService.loginMember(request);
    }

    @PostMapping("/signup")
    public MemberCreateResponse saveMember(@RequestBody @Validated MemberCreateRequest request) {
        return memberService.signUp(request);
    }

    //유저 정보 상세 보기
    @GetMapping("/user/{memberId}/view")
    public MemberViewInfoResponse userViewInformation(@PathVariable("memberId") Long memberId) {
        return memberService.userViewMember(memberId);
    }

    //유저 정보 수정하기
    @PutMapping("/user/{memberId}/modify")
    public void userModifyInformation(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Validated MemberModifyRequest request
    ) {
        memberService.modifyMember(memberId, request);
    }

}
