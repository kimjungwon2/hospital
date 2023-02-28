package site.hospital.member.user.repository.dto;

import lombok.Data;
import site.hospital.member.user.domain.MemberStatus;

@Data
public class LoginMemberResponse {

    Long memberId;
    String nickName;
    MemberStatus memberStatus;
    String token;

    public LoginMemberResponse(
            Long memberId,
            String nickName,
            MemberStatus memberStatus,
            String token
    ) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.memberStatus = memberStatus;
        this.token = token;
    }
}