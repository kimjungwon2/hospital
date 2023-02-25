package site.hospital.question.manager.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.question.manager.service.ManagerQuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerQuestionController {

    private final ManagerQuestionService managerQuestionService;

    @GetMapping("/staff/question/search")
    public Page managerSearchHospitalQuestions(ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return managerQuestionService
                .searchHospitalQuestions(servletRequest, nickName, memberIdName, pageable);
    }

    @GetMapping("/staff/question/noAnswer/search")
    public Page managerSearchNoQuestions(
            ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return managerQuestionService
                .searchNoQuestions(servletRequest, nickName, memberIdName, pageable);
    }

    @GetMapping("/staff/question/count")
    public Long managerQuestionNoAnswer(ServletRequest servletRequest) {
        return managerQuestionService.countQuestionsWithNoAnswer(servletRequest);
    }
}
