package site.hospital.member.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.member.admin.api.dto.MemberAdminCreateRequest;
import site.hospital.member.admin.api.dto.MemberAdminModifyRequest;
import site.hospital.member.admin.api.dto.MemberAdminViewInfoResponse;
import site.hospital.member.user.api.dto.MemberCreateResponse;
import site.hospital.member.user.api.dto.MemberSearchResponse;
import site.hospital.member.user.domain.MemberStatus;

public interface AdminMemberService {

    void deleteMember(Long memberId);

    void modifyMember(Long memberId, MemberAdminModifyRequest request);

    Page<MemberSearchResponse> searchMembers(
            String allSearch,
            Long memberId,
            String memberIdName,
            String nickName,
            String userName,
            String phoneNumber,
            MemberStatus memberStatus,
            Long hospitalNumber,
            Pageable pageable
    );

    MemberCreateResponse signup(MemberAdminCreateRequest request);

    MemberAdminViewInfoResponse viewMemberInformation(Long memberId);

}
