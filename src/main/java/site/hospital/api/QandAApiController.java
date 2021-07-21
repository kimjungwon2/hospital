package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.QandA;
import site.hospital.repository.qandA.simpleQuery.SearchHospitalQandADTO;
import site.hospital.service.QandAService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class QandAApiController {
    private final QandAService qandAService;

    //QandA 생성
    @PostMapping("hospital/qanda/register")
    public CreateQandAResponse createQandA(@RequestBody @Validated CreateQandARequest request){
        Long id = qandAService.qandACreate(request.getMemberId(),request.getHospitalId(),request.getContent());
        return new CreateQandAResponse(id);
    }

    //병원 QandA 조회.
    @GetMapping("/hospital/qanda/{hospitalId}")
    public List<SearchHospitalQandADTO>  searchHospitalQandA(@PathVariable("hospitalId") Long hospitalId){
        return qandAService.searchHospitalQandA(hospitalId);
    }


    /* DTO */
    @Data
    private static class CreateQandAResponse{
        Long id;
        public CreateQandAResponse(long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateQandARequest{
        Long hospitalId;
        Long memberId;
        private String content;
    }

    @Data
    private static class SearchHospitalQandAResponse{
        private Long reviewId;
        private String nickName;
        private String content;
        private Long answerId;
        private String answerContent;

        public SearchHospitalQandAResponse(QandA qandA) {
            this.reviewId = qandA.getId();
            this.nickName = qandA.getMember().getNickName();
            this.content = qandA.getContent();

            this.answerId = qandA.getAnswer().getId();
            this.answerContent = qandA.getAnswer().getAnswerContent();

        }
    }

}
