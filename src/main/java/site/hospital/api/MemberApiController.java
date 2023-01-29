package site.hospital.api;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import site.hospital.domain.member.Member;
import site.hospital.domain.member.MemberStatus;
import site.hospital.dto.AdminMemberSearchCondition;
import site.hospital.jwtToken.CustomUserDetail;
import site.hospital.jwtToken.JwtFilter;
import site.hospital.jwtToken.TokenProvider;
import site.hospital.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> loginMember(
            @RequestBody @Validated MemberLoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getMemberIdName(),
                        request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //커스텀 사용자 객체 가져오기
        CustomUserDetail user =
                (CustomUserDetail) authentication.getPrincipal();

        //jwt Token
        String jwt = GetJWToken(authentication, user);

        //토큰 null 체크.
        if (jwt == null) {
            throw new IllegalStateException("토큰 값이 null 입니다.");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(MemberLoginResponse
                .from(user.getMemberId(), user.getNickName(), user.getMemberStatus(), jwt),
                httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public MemberCreateResponse saveMember(@RequestBody @Validated MemberCreateRequest request) {
        Member member = Member.builder()
                .memberIdName(request.getMemberIdName())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .userName(request.getUserName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        Long id = memberService.signUp(member);

        return MemberCreateResponse.from(id);
    }

    //유저 정보 상세 보기
    @GetMapping("/user/{memberId}/view")
    public MemberViewInfoResponse userViewInformation(@PathVariable("memberId") Long memberId) {
        Member member = memberService.viewMember(memberId);
        MemberViewInfoResponse viewMemberInformation = MemberViewInfoResponse.from(member);

        return viewMemberInformation;
    }


    //유저 정보 수정하기
    @PutMapping("/user/{memberId}/modify")
    public void userModifyInformation(@PathVariable("memberId") Long memberId,
            @RequestBody @Validated MemberModifyRequest request) {
        Member member = Member.builder().phoneNumber(request.getPhoneNumber())
                .nickName(request.getNickName())
                .userName(request.getUserName()).build();

        memberService.modifyMember(memberId, member);
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
            Pageable pageable) {

        //받은 값들 생성자로 생성.
        AdminMemberSearchCondition condition = AdminMemberSearchCondition.builder()
                .allSearch(allSearch).memberId(memberId).memberIdName(memberIdName)
                .nickName(nickName)
                .userName(userName).phoneNumber(phoneNumber).memberStatus(memberStatus)
                .hospitalNumber(hospitalNumber).build();

        Page<Member> members = memberService.adminSearchMembers(condition, pageable);
        List<MemberSearchResponse> result = members.stream()
                .map(m -> MemberSearchResponse.from(m)).collect(Collectors.toList());

        Long total = members.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //관리자 유저 상세 보기
    @GetMapping("/admin/user/view/{memberId}")
    public MemberAdminViewInfoResponse adminViewMember(@PathVariable("memberId") Long memberId) {
        Member member = memberService.viewMember(memberId);
        MemberAdminViewInfoResponse adminViewMember = MemberAdminViewInfoResponse.from(member);

        return adminViewMember;
    }

    //관리자 멤버 생성
    @PostMapping("/admin/signup")
    public MemberCreateResponse adminSaveMember(
            @RequestBody @Validated MemberAdminCreateRequest request) {
        Member member = Member.builder()
                .memberIdName(request.getMemberIdName())
                .password(request.getPassword())
                .nickName(request.getNickName())
                .userName(request.getUserName())
                .memberStatus(request.getMemberStatus())
                .phoneNumber(request.getPhoneNumber())
                .hospitalNumber(request.getHospitalId())
                .build();
        Long id = memberService.adminSignUp(member);

        return MemberCreateResponse.from(id);
    }

    //관리자 멤버 삭제
    @DeleteMapping("/admin/user/delete/{memberId}")
    public void adminDeleteMember(@PathVariable("memberId") Long memberId) {
        memberService.adminDeleteMember(memberId);
    }

    //관리자 멤버 수정하기
    @PutMapping("/admin/user/modify/{memberId}")
    public void adminModifyMember(@PathVariable("memberId") Long memberId,
            @RequestBody @Validated MemberAdminModifyRequest request) {
        Member member = Member.builder().phoneNumber(request.getPhoneNumber())
                .memberStatus(request.getMemberStatus())
                .nickName(request.getNickName())
                .hospitalNumber(request.getHospitalId())
                .userName(request.getUserName())
                .build();

        memberService.adminModifyMember(memberId, member);
    }

    //토큰 획득
    private String GetJWToken(Authentication authentication, CustomUserDetail user) {
        //멤버 권한이 일반 유저라면
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")) &&

                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
        ) {
            return tokenProvider.createToken(authentication, user.getPhoneNumber());
        }
        //멤버 권한이 STAFF 전용 토큰 만들기
        else if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&

                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))
        ) {
            return tokenProvider.createStaffToken(authentication,
                    user.getPhoneNumber(), user.getHospitalNumber());
        }
        //멤버 권한이 관리자라면
        else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return tokenProvider.createToken(authentication, user.getPhoneNumber());
        } else {
            throw new IllegalStateException("권한이 존재하지 않습니다.");
        }
    }

}
