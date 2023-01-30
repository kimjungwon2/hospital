package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.member.MemberAdminCreateRequest;
import site.hospital.api.dto.member.MemberAdminModifyRequest;
import site.hospital.api.dto.member.MemberAdminViewInfoResponse;
import site.hospital.api.dto.member.MemberCreateRequest;
import site.hospital.api.dto.member.MemberCreateResponse;
import site.hospital.api.dto.member.MemberLoginRequest;
import site.hospital.api.dto.member.MemberLoginResponse;
import site.hospital.api.dto.member.MemberModifyRequest;
import site.hospital.api.dto.member.MemberSearchResponse;
import site.hospital.api.dto.member.MemberViewInfoResponse;
import site.hospital.domain.member.MemberStatus;
import site.hospital.service.MemberService;

@RestController
@RequiredArgsConstructor
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

    //관리자 유저 검색
    @GetMapping("/admin/user/search")
    public Page<MemberSearchResponse> adminSearchMembers(
            @RequestParam(value = "allSearch", required = false) String allSearch,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "authorizationStatus", required = false) MemberStatus memberStatus,
            @RequestParam(value = "hospitalNumber", required = false) Long hospitalNumber,
            Pageable pageable
    ) {
        return memberService.adminSearchMembers(allSearch, memberId, memberIdName,
                nickName, userName, phoneNumber, memberStatus,
                hospitalNumber, pageable);
    }

    //관리자 유저 상세 보기
    @GetMapping("/admin/user/view/{memberId}")
    public MemberAdminViewInfoResponse adminViewMember(@PathVariable("memberId") Long memberId) {
        return memberService.adminViewMember(memberId);
    }

    //관리자 멤버 생성
    @PostMapping("/admin/signup")
    public MemberCreateResponse adminSaveMember(
            @RequestBody @Validated MemberAdminCreateRequest request) {
        return memberService.adminSignUp(request);
    }

    //관리자 멤버 삭제
    @DeleteMapping("/admin/user/delete/{memberId}")
    public void adminDeleteMember(@PathVariable("memberId") Long memberId) {
        memberService.adminDeleteMember(memberId);
    }

    //관리자 멤버 수정하기
    @PutMapping("/admin/user/modify/{memberId}")
    public void adminModifyMember(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Validated MemberAdminModifyRequest request
    ) {
        memberService.adminModifyMember(memberId, request);
    }

}
