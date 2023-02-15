package site.hospital.answer.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.answer.api.dto.AnswerCreateRequest;
import site.hospital.answer.api.dto.AnswerCreateResponse;
import site.hospital.answer.service.AnswerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnswerApiController {

    private final AnswerService answerService;

    @PostMapping("/staff/question/answer")
    public AnswerCreateResponse registerAnswer(
            ServletRequest servletRequest,
            @RequestBody @Validated AnswerCreateRequest request
    ) {
        Long id = answerService
                .registerAnswer(servletRequest, request);

        return AnswerCreateResponse.from(id);
    }

}
