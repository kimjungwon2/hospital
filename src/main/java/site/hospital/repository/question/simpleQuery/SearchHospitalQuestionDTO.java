package site.hospital.repository.question.simpleQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SearchHospitalQuestionDTO {

    private Long questionId;
    private String nickName;
    private String content;
    private Long answerId;
    private String answerContent;

    @QueryProjection
    public SearchHospitalQuestionDTO(Long questionId, String nickName, String content,
            Long answerId, String answerContent) {
        this.questionId = questionId;
        this.nickName = nickName;
        this.content = content;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }
}
