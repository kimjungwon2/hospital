package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Question;
import site.hospital.dto.AdminQuestionSearchCondition;
import site.hospital.repository.question.adminSearchQuery.AdminSearchQuestionDto;
import site.hospital.repository.question.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.repository.question.userQuery.SearchUserQuestionDTO;
import site.hospital.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class QuestionApiController {
    private final QuestionService questionService;

    //Question 생성
    @PostMapping("/hospital/question/register")
    public CreateQuestionResponse createQuestion(@RequestBody @Validated CreateQuestionRequest request){
        Long id = questionService.questionCreate(request.getMemberId(),request.getHospitalId(),request.getContent());
        return new CreateQuestionResponse(id);
    }

    //병원 Question 조회.
    @GetMapping("/hospital/question/{hospitalId}")
    public List<SearchHospitalQuestionDTO>  searchHospitalQuestion(@PathVariable("hospitalId") Long hospitalId){
        return questionService.searchHospitalQuestion(hospitalId);
    }

    //유저 Question 조회
    @GetMapping("/user/{memberId}/questions")
    public List<SearchUserQuestionDTO>  searchUserQuestion(@PathVariable("memberId") Long memberId){
        return questionService.searchUserQuestion(memberId);
    }


    //관리자 Question 조회
    @GetMapping("/admin/questions")
    public Page<AdminSearchQuestionDto> adminQuestions(Pageable pageable){
        return questionService.adminQuestions(pageable);
    }

    //관리자 Questions 검색
    @GetMapping("/admin/questions/search")
    public Page<AdminSearchQuestionDto> adminSearchQuestions(@RequestParam(value="nickName",required = false) String nickName,
                                                             @RequestParam(value="hospitalName",required = false) String hospitalName,
                                                             @RequestParam(value="memberIdName",required = false) String memberIdName,
                                                             Pageable pageable){
        AdminQuestionSearchCondition condition = AdminQuestionSearchCondition.builder()
                .nickName(nickName).hospitalName(hospitalName).memberIdName(memberIdName).build();

        return questionService.adminSearchQuestions(condition,pageable);
    }

    //관리자 Question 삭제
    @DeleteMapping("/admin/question/delete")
    public void deleteQuestion(@RequestBody @Validated DeleteQuestionRequest request){
        questionService.questionDelete(request.getQuestionId(),request.getAnswerId());
    }



    /* DTO */
    @Data
    private static class CreateQuestionResponse{
        Long id;
        public CreateQuestionResponse(long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateQuestionRequest{
        Long hospitalId;
        Long memberId;
        private String content;
    }

    @Data
    private static class SearchHospitalQuestionResponse{
        private Long reviewId;
        private String nickName;
        private String content;
        private Long answerId;
        private String answerContent;

        public SearchHospitalQuestionResponse(Question question) {
            this.reviewId = question.getId();
            this.nickName = question.getMember().getNickName();
            this.content = question.getContent();

            this.answerId = question.getAnswer().getId();
            this.answerContent = question.getAnswer().getAnswerContent();

        }
    }

    @Data
    private static class DeleteQuestionRequest{
        private Long questionId;
        private Long answerId;
    }


}
