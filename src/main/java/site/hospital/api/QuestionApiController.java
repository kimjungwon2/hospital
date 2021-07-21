package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Question;
import site.hospital.repository.question.simpleQuery.SearchHospitalQuestionDTO;
import site.hospital.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:8080")
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

    //병원 Question 조회.
    @GetMapping("/hospital/question2/{hospitalId}")
    public List<SearchHospitalQuestionResponse>  searchHospitalQandA2(@PathVariable("hospitalId") Long hospitalId){
        List<Question> questions = questionService.searchHospitalQandA2(hospitalId);
        List<SearchHospitalQuestionResponse> result = questions.stream()
                .map(q->new SearchHospitalQuestionResponse(q))
                .collect(Collectors.toList());
        return result;
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

}
