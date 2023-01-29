package site.hospital.api.dto.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.member.MemberStatus;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MemberLoginResponse {

    private final Long memberId;
    private final String nickName;
    private final MemberStatus memberStatus;
    private final String token;

    public static MemberLoginResponse from(Long memberId, String nickName,
            MemberStatus memberStatus,
            String token) {
        return MemberLoginResponse
                .builder()
                .memberId(memberId)
                .nickName(nickName)
                .memberStatus(memberStatus)
                .token(token)
                .build();
    }
}
