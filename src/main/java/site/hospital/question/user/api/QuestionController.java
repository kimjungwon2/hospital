package site.hospital.question.user.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.question.user.api.dto.QuestionCreateRequest;
import site.hospital.question.user.api.dto.QuestionCreateResponse;
import site.hospital.question.user.repository.inquiry.UserQuestionSelectQuery;
import site.hospital.question.user.repository.hospital.HospitalQuestionSelectQuery;
import site.hospital.question.user.service.QuestionService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/user/hospital/question/register")
    public QuestionCreateResponse createQuestion(
            @RequestBody @Validated QuestionCreateRequest request
    ) {
        return questionService.createQuestion(request);
    }

    @GetMapping("/hospital/question/{hospitalId}")
    public List<HospitalQuestionSelectQuery> inquireHospitalQuestions(
            @PathVariable("hospitalId") Long hospitalId) {
        return questionService.inquireHospitalQuestions(hospitalId);
    }

    @GetMapping("/user/{memberId}/questions")
    public List<UserQuestionSelectQuery> inquireQuestionsByUser(@PathVariable("memberId") Long memberId) {
        return questionService.inquireQuestionsByUser(memberId);
    }

}
