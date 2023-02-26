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
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody @Validated MemberLoginRequest request) {
        return memberService.login(request);
    }

    @PostMapping("/signup")
    public MemberCreateResponse signup(@RequestBody @Validated MemberCreateRequest request) {
        return memberService.signup(request);
    }

    @GetMapping("/user/{memberId}/view")
    public MemberViewInfoResponse viewUserInformation(@PathVariable("memberId") Long memberId) {
        return memberService.viewUserInformation(memberId);
    }

    @PutMapping("/user/{memberId}/modify")
    public void modifyMemberByUser(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Validated MemberModifyRequest request
    ) {
        memberService.modifyMemberByUser(memberId, request);
    }

}
