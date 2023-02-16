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

    //병원 관계자 Questions 검색
    @GetMapping("/staff/question/search")
    public Page staffSearchQuestions(ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return managerQuestionService
                .staffSearchHospitalQuestion(servletRequest, nickName, memberIdName, pageable);
    }

    //병원 관계자 답변 없는 Questions 검색
    @GetMapping("/staff/question/noAnswer/search")
    public Page staffSearchNoQuestion(
            ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return managerQuestionService
                .staffSearchNoQuestion(servletRequest, nickName, memberIdName, pageable);
    }

    //병원 관계자 미답변 question 수 받아오기
    @GetMapping("/staff/question/count")
    public Long staffQuestionNoAnswer(ServletRequest servletRequest) {
        return managerQuestionService.staffQuestionNoAnswer(servletRequest);
    }
}
