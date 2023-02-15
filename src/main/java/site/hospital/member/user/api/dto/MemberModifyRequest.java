package site.hospital.member.user.api.dto;

import lombok.Data;

@Data
public class MemberModifyRequest {

    private String nickName;
    private String phoneNumber;
    private String userName;
}
