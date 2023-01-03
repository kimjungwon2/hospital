package site.hospital.api;

import javax.servlet.ServletRequest;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.Answer;
import site.hospital.service.AnswerService;

@RestController
@RequiredArgsConstructor
public class AnswerApiController {

    private final AnswerService answerService;

    @PostMapping("/staff/question/answer")
    public CreateAnswerResponse registerAnswer(ServletRequest servletRequest,
            @RequestBody @Validated CreateAnswerRequest request) {
        Answer answer = Answer.builder().answerContent(request.getAnswerContent()).build();
        Long id = answerService
                .registerAnswer(servletRequest, request.getMemberId(), request.getQuestionId(),
                        answer);

        return new CreateAnswerResponse(id);
    }

    /*DTO */
    @Data
    private static class CreateAnswerResponse {

        private Long answerId;

        public CreateAnswerResponse(Long answerId) {
            this.answerId = answerId;
        }
    }

    @Data
    private static class CreateAnswerRequest {

        @NotNull(message = "멤버 번호가 필요합니다.")
        private Long memberId;
        @NotNull(message = "질문 번호가 필요합니다.")
        private Long questionId;
        @NotNull(message = "답변 내용을 입력해주세요.")
        private String answerContent;
    }
}
