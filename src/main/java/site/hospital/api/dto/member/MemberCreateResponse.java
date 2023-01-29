package site.hospital.api.dto.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MemberCreateResponse {

    private final Long memberId;

    public static MemberCreateResponse from(long memberId) {
        return MemberCreateResponse
                .builder()
                .memberId(memberId)
                .build();
    }
}
