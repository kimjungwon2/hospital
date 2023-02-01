package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.member.MemberAdminViewInfoResponse;
import site.hospital.service.MemberService;

@RestController
@RequiredArgsConstructor
public class TestApiController {

    private final MemberService memberService;

    @GetMapping("/test")
    public String test1(){
        return "hello";
    }

    @GetMapping("/test/{memberId}")
    public MemberAdminViewInfoResponse test2(@PathVariable("memberId") Long memberId) {
        return memberService.adminViewMember(memberId);
    }

}
