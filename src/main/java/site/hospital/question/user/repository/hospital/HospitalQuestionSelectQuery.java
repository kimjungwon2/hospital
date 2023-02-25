package site.hospital.question.user.repository.hospital;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalQuestionSelectQuery {

    private Long questionId;
    private String nickName;
    private String content;
    private Long answerId;
    private String answerContent;

    @QueryProjection
    public HospitalQuestionSelectQuery(
            Long questionId,
            String nickName,
            String content,
            Long answerId,
            String answerContent
    ) {
        this.questionId = questionId;
        this.nickName = nickName;
        this.content = content;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }
}
