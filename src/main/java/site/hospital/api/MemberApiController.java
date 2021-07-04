package site.hospital.api;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.CreateMemberRequest;
import site.hospital.api.dto.CreateMemberResponse;
import site.hospital.domain.Member;
import site.hospital.dto.MemberSearchCondition;
import site.hospital.dto.MemberSearchResult;
import site.hospital.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/member/signup")
    public CreateMemberResponse saveMember(@RequestBody @Validated CreateMemberRequest request){
        Member member= Member.builder()
                .memberIdName(request.getMemberIdName())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .userName(request.getUserName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        Long id =memberService.signUp(member);

        return new CreateMemberResponse(id);
    }

    @GetMapping("/member/search")
    public Page<MemberSearchResult> searchMember(MemberSearchCondition condition, Pageable pageable){
        return memberService.search(condition, pageable);
    }

}
