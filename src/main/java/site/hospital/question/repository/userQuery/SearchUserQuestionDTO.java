package site.hospital.question.repository.userQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SearchUserQuestionDTO {

    private Long questionId;
    private Long hospitalId;
    private String hospitalName;
    private String nickName;
    private String content;
    private Long answerId;
    private String answerContent;

    @QueryProjection
    public SearchUserQuestionDTO(Long questionId, Long hospitalId, String hospitalName,
            String nickName, String content, Long answerId, String answerContent) {
        this.questionId = questionId;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.nickName = nickName;
        this.content = content;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }
}
