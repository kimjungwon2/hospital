package site.hospital.member.user.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberStatus;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MemberSearchResponse {
    private final Long memberId;
    private final String memberIdName;
    private final String userName;
    private final MemberStatus memberStatus;
    private final Long hospitalNumber;

    public static MemberSearchResponse from(Member member) {
        return MemberSearchResponse
                .builder()
                .memberId(member.getId())
                .memberIdName(member.getMemberIdName())
                .userName(member.getUserName())
                .memberStatus(member.getMemberStatus())
                .hospitalNumber(member.getHospitalNumber())
                .build();
    }
}
