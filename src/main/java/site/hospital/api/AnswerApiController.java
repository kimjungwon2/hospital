package site.hospital.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.answer.AnswerCreateRequest;
import site.hospital.api.dto.answer.AnswerCreateResponse;
import site.hospital.domain.Answer;
import site.hospital.service.AnswerService;

@RestController
@RequiredArgsConstructor
public class AnswerApiController {

    private final AnswerService answerService;

    @PostMapping("/staff/question/answer")
    public AnswerCreateResponse registerAnswer(ServletRequest servletRequest,
            @RequestBody @Validated AnswerCreateRequest request) {
        Answer answer = Answer.builder().answerContent(request.getAnswerContent()).build();

        Long id = answerService
                .registerAnswer(servletRequest, request.getMemberId(), request.getQuestionId(),
                        answer);

        return AnswerCreateResponse.from(id);
    }


}
