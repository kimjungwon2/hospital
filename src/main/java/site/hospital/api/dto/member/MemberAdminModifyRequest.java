package site.hospital.api.dto.member;

import javax.validation.constraints.NotNull;
import lombok.Data;
import site.hospital.domain.member.MemberStatus;

@Data
public class MemberAdminModifyRequest {

    @NotNull(message = "권한을 넣어주세요.")
    private MemberStatus memberStatus;
    private String nickName;
    private String phoneNumber;
    private String userName;
    private Long hospitalId;
}
