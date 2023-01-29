package site.hospital.api.dto.question;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionCreateRequest {

    @NotNull(message = "병원 번호를 입력해주세요.")
    private Long hospitalId;
    @NotNull(message = "멤버 번호를 입력해주세요.")
    private Long memberId;
    @NotNull(message = "질문 내용을 입력해주세요.")
    private String content;
}
