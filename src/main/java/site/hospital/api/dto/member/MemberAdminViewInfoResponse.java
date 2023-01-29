package site.hospital.api.dto.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.member.Member;
import site.hospital.domain.member.MemberStatus;

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
