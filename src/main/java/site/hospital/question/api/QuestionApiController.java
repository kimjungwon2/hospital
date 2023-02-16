package site.hospital.question.api;

import java.util.List;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.question.api.dto.QuestionCreateRequest;
import site.hospital.question.api.dto.QuestionCreateResponse;
import site.hospital.question.repository.adminSearchQuery.AdminSearchQuestionDto;
import site.hospital.question.repository.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.question.repository.userQuery.SearchUserQuestionDTO;
import site.hospital.question.service.QuestionService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestionApiController {

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

    //병원 관계자 Questions 검색
    @GetMapping("/staff/question/search")
    public Page staffSearchQuestions(ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return questionService
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
        return questionService
                .staffSearchNoQuestion(servletRequest, nickName, memberIdName, pageable);
    }

    //병원 관계자 미답변 question 수 받아오기
    @GetMapping("/staff/question/count")
    public Long staffQuestionNoAnswer(ServletRequest servletRequest) {
        return questionService.staffQuestionNoAnswer(servletRequest);
    }

    //관리자 Questions 검색
    @GetMapping("/admin/question/search")
    public Page<AdminSearchQuestionDto> adminSearchQuestions(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return questionService.adminSearchQuestions(nickName, hospitalName, memberIdName, pageable);
    }

    //관리자 Question 삭제
    @DeleteMapping("/admin/question/delete")
    public void deleteQuestion(
            @RequestParam(value = "questionId", required = false) Long questionId,
            @RequestParam(value = "answerId", required = false) Long answerId
    ) {
        questionService.questionDelete(questionId, answerId);
    }

}
