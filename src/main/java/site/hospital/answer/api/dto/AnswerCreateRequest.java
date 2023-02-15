package site.hospital.answer.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerCreateRequest {

    @NotNull(message = "멤버 번호가 필요합니다.")
    private Long memberId;
    @NotNull(message = "질문 번호가 필요합니다.")
    private Long questionId;
    @NotNull(message = "답변 내용을 입력해주세요.")
    private String answerContent;
}
