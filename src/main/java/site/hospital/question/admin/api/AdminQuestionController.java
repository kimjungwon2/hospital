package site.hospital.question.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.question.admin.service.AdminQuestionService;
import site.hospital.question.user.repository.adminSearchQuery.AdminSearchQuestionDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;

    //관리자 Questions 검색
    @GetMapping("/admin/question/search")
    public Page<AdminSearchQuestionDto> adminSearchQuestions(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return adminQuestionService.adminSearchQuestions(nickName, hospitalName, memberIdName, pageable);
    }

    //관리자 Question 삭제
    @DeleteMapping("/admin/question/delete")
    public void deleteQuestion(
            @RequestParam(value = "questionId", required = false) Long questionId,
            @RequestParam(value = "answerId", required = false) Long answerId
    ) {
        adminQuestionService.questionDelete(questionId, answerId);
    }

}
