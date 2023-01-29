package site.hospital.api.dto.member;

import lombok.Data;

@Data
public class MemberModifyRequest {

    private String nickName;
    private String phoneNumber;
    private String userName;
}
