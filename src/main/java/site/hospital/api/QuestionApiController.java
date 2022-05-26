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
import site.hospital.dto.StaffQuestionSearchCondition;
import site.hospital.repository.question.adminSearchQuery.AdminSearchQuestionDto;
import site.hospital.repository.question.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.repository.question.userQuery.SearchUserQuestionDTO;
import site.hospital.service.QuestionService;

import javax.servlet.ServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class QuestionApiController {
    private final QuestionService questionService;

    //Question 생성
    @PostMapping("/user/hospital/question/register")
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

    //병원 관계자 Questions 검색
    @GetMapping("/staff/question/search")
    public Page staffSearchQuestions(ServletRequest servletRequest,
                                               @RequestParam(value="nickName",required = false) String nickName,
                                               @RequestParam(value="memberIdName",required = false) String memberIdName,
                                               Pageable pageable){
        StaffQuestionSearchCondition condition = StaffQuestionSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).build();

        Page<Question> questions = questionService.staffSearchHospitalQuestion(servletRequest,condition,pageable);

        List<SearchHospitalQuestionResponse> result = questions.stream()
                .map(q->new SearchHospitalQuestionResponse(q))
                .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //병원 관계자 답변 없는 Questions 검색
    @GetMapping("/staff/question/noAnswer/search")
    public Page staffSearchNoQuestion(ServletRequest servletRequest,
                                     @RequestParam(value="nickName",required = false) String nickName,
                                     @RequestParam(value="memberIdName",required = false) String memberIdName,
                                     Pageable pageable){
        StaffQuestionSearchCondition condition = StaffQuestionSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).build();

        Page<Question> questions = questionService.staffSearchNoQuestion(servletRequest,condition,pageable);

        List<SearchHospitalQuestionResponse> result = questions.stream()
                .map(q->new SearchHospitalQuestionResponse(q))
                .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //병원 관계자 미답변 question 수 받아오기
    @GetMapping("/staff/question/count")
    public Long staffQuestionNoAnswer(ServletRequest servletRequest){
        Long questionCount = questionService.staffQuestionNoAnswer(servletRequest);

        return questionCount;
    }

    //관리자 Questions 검색
    @GetMapping("/admin/question/search")
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
    public void deleteQuestion(@RequestParam(value="questionId",required = false) Long questionId,
                               @RequestParam(value="answerId",required = false) Long answerId){
        questionService.questionDelete(questionId,answerId);
    }



    /* DTO */
    @Data
    private static class CreateQuestionResponse{
        private Long id;
        public CreateQuestionResponse(long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateQuestionRequest{
        @NotNull(message="병원 번호를 입력해주세요.")
        private Long hospitalId;
        @NotNull(message="멤버 번호를 입력해주세요.")
        private Long memberId;
        @NotNull(message="질문 내용을 입력해주세요.")
        private String content;
    }

    @Data
    private static class SearchHospitalQuestionResponse{
        private Long questionId;
        private String memberIdName;
        private String nickName;
        private String content;
        private Long answerId;
        private String answerContent;

        public SearchHospitalQuestionResponse(Question question) {
            this.memberIdName = question.getMember().getMemberIdName();
            this.questionId = question.getId();
            this.nickName = question.getMember().getNickName();
            this.content = question.getContent();

            if(question.getAnswer() !=null) {
                this.answerId = question.getAnswer().getId();
                this.answerContent = question.getAnswer().getAnswerContent();
            }
        }
    }

}
