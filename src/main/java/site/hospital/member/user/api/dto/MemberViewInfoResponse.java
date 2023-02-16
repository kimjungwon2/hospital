package site.hospital.member.user.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberStatus;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MemberViewInfoResponse {

    private final Long id;
    private final String memberIdName;
    private final String userName;
    private final String nickName;
    private final String phoneNumber;
    private final MemberStatus memberStatus;

    public static MemberViewInfoResponse from(Member member) {
        return MemberViewInfoResponse
                .builder()
                .id(member.getId())
                .memberIdName(member.getMemberIdName())
                .userName(member.getUserName())
                .nickName(member.getNickName())
                .phoneNumber(member.getPhoneNumber())
                .memberStatus(member.getMemberStatus())
                .build();
    }

}
