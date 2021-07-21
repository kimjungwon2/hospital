package site.hospital.api;

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

    @PostMapping
    public CreateAnswerResponse registerAnswer(@RequestBody @Validated CreateAnswerRequest request){
        Answer answer = Answer.builder().answerContent(request.getAnswerContent()).build();
        Long id = answerService.registerAnswer(request.getMemberId(),request.getQandaId(),answer);

        return new CreateAnswerResponse(id);
    }

    /*DTO */
    @Data
    private static class CreateAnswerResponse{
        private Long answerId;
        public CreateAnswerResponse(Long answerId) {
            this.answerId = answerId;
        }
    }

    @Data
    private static class CreateAnswerRequest{
        private Long memberId;
        private Long qandaId;
        private String answerContent;
    }
}
