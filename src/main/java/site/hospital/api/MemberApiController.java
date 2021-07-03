package site.hospital.api;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


}
