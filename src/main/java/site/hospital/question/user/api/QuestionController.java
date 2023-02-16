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
import site.hospital.question.user.repository.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.question.user.repository.userQuery.SearchUserQuestionDTO;
import site.hospital.question.user.service.QuestionService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;

    //Question 생성
    @PostMapping("/user/hospital/question/register")
    public QuestionCreateResponse createQuestion(
            @RequestBody @Validated QuestionCreateRequest request) {
        return questionService.questionCreate(request);
    }

    //병원 Question 조회.
    @GetMapping("/hospital/question/{hospitalId}")
    public List<SearchHospitalQuestionDTO> searchHospitalQuestion(
            @PathVariable("hospitalId") Long hospitalId) {
        return questionService.searchHospitalQuestion(hospitalId);
    }

    //유저 Question 조회
    @GetMapping("/user/{memberId}/questions")
    public List<SearchUserQuestionDTO> searchUserQuestion(@PathVariable("memberId") Long memberId) {
        return questionService.searchUserQuestion(memberId);
    }



}
