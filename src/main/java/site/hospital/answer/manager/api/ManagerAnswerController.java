package site.hospital.answer.manager.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.answer.manager.api.dto.AnswerCreateRequest;
import site.hospital.answer.manager.api.dto.AnswerCreateResponse;
import site.hospital.answer.manager.service.ManagerAnswerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerAnswerController {

    private final ManagerAnswerService managerAnswerService;

    @PostMapping("/staff/question/answer")
    public AnswerCreateResponse registerAnswer(
            ServletRequest servletRequest,
            @RequestBody @Validated AnswerCreateRequest request
    ) {
        Long id = managerAnswerService
                  .registerAnswer(servletRequest, request);

        return AnswerCreateResponse.from(id);
    }

}
