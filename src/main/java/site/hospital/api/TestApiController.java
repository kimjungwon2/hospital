package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.member.MemberAdminViewInfoResponse;
import site.hospital.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestApiController {

    private final MemberService memberService;

    @GetMapping("/test")
    public String test1(){
        return "CI/CD 완료 2.";
    }

    @GetMapping("/test/{memberId}")
    public MemberAdminViewInfoResponse test2(@PathVariable("memberId") Long memberId) {
        return memberService.adminViewMember(memberId);
    }

}
