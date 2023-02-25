package site.hospital.question.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.question.admin.repository.search.AdminQuestionSearchSelectQuery;
import site.hospital.question.admin.service.AdminQuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;
    
    @GetMapping("/admin/question/search")
    public Page<AdminQuestionSearchSelectQuery> adminSearchQuestions(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return adminQuestionService.searchQuestions(nickName, hospitalName, memberIdName, pageable);
    }
    
    @DeleteMapping("/admin/question/delete")
    public void adminDeleteQuestion(
            @RequestParam(value = "questionId", required = false) Long questionId,
            @RequestParam(value = "answerId", required = false) Long answerId
    ) {
        adminQuestionService.deleteQuestion(questionId, answerId);
    }

}
