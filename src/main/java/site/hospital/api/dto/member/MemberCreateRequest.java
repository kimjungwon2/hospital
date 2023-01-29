package site.hospital.api.dto.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberCreateRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private String userName;
    @Email(message = "올바른 이메일 형태가 아닙니다.")
    @NotBlank(message = "공백없이 아이디를 입력해주세요.")
    private String memberIdName;
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickName;
    @NotNull(message = "전화번호를 입력해주세요.")
    private String phoneNumber;
}
