package site.hospital.member.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.member.admin.api.dto.MemberAdminCreateRequest;
import site.hospital.member.admin.api.dto.MemberAdminModifyRequest;
import site.hospital.member.admin.api.dto.MemberAdminViewInfoResponse;
import site.hospital.member.admin.service.AdminMemberService;
import site.hospital.member.user.api.dto.MemberCreateResponse;
import site.hospital.member.user.api.dto.MemberSearchResponse;
import site.hospital.member.user.domain.MemberStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

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
        return adminMemberService.searchMembers(
                allSearch,
                memberId,
                memberIdName,
                nickName,
                userName,
                phoneNumber,
                memberStatus,
                hospitalNumber,
                pageable);
    }

    @GetMapping("/admin/user/view/{memberId}")
    public MemberAdminViewInfoResponse adminViewMember(@PathVariable("memberId") Long memberId) {
        return adminMemberService.viewMemberInformation(memberId);
    }

    @PostMapping("/admin/signup")
    public MemberCreateResponse adminSaveMember(
            @RequestBody @Validated MemberAdminCreateRequest request) {
        return adminMemberService.signup(request);
    }

    @DeleteMapping("/admin/user/delete/{memberId}")
    public void adminDeleteMember(@PathVariable("memberId") Long memberId) {
        adminMemberService.deleteMember(memberId);
    }

    @PutMapping("/admin/user/modify/{memberId}")
    public void adminModifyMember(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Validated MemberAdminModifyRequest request
    ) {
        adminMemberService.modifyMember(memberId, request);
    }

}
