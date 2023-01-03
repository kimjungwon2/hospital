package site.hospital.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginMemberRequest {

    @Email(message = "올바른 이메일 형태가 아닙니다.")
    @NotBlank(message = "공백없이 아이디를 입력해주세요.")
    String memberIdName;

    @NotNull(message = "비밀번호를 입력해주세요.")
    String password;
}
