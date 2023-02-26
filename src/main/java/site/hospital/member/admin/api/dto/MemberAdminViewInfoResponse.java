package site.hospital.member.admin.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberStatus;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MemberAdminViewInfoResponse {

    private final Long id;
    private final String memberIdName;
    private final String userName;
    private final String nickName;
    private final String phoneNumber;
    private final Long hospitalNumber;
    private final MemberStatus memberStatus;

    public static MemberAdminViewInfoResponse from(Member member) {
        return MemberAdminViewInfoResponse
                .builder()
                .id(member.getId())
                .memberIdName(member.getMemberIdName())
                .userName(member.getUserName())
                .nickName(member.getNickName())
                .phoneNumber(member.getPhoneNumber())
                .hospitalNumber(member.getHospitalNumber())
                .memberStatus(member.getMemberStatus())
                .build();
    }
}
