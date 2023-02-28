package site.hospital.member.user.service;

import org.springframework.http.ResponseEntity;
import site.hospital.member.user.api.dto.MemberCreateRequest;
import site.hospital.member.user.api.dto.MemberCreateResponse;
import site.hospital.member.user.api.dto.MemberLoginRequest;
import site.hospital.member.user.api.dto.MemberLoginResponse;
import site.hospital.member.user.api.dto.MemberModifyRequest;
import site.hospital.member.user.api.dto.MemberViewInfoResponse;
import site.hospital.member.user.domain.Member;

public interface MemberService {

    ResponseEntity<MemberLoginResponse> login(MemberLoginRequest request);

    MemberCreateResponse signup(MemberCreateRequest request);

    void modifyMemberByUser(Long memberId, MemberModifyRequest request);

    MemberViewInfoResponse viewUserInformation(Long memberId);

    void validateDuplicateMember(Member member);

}
