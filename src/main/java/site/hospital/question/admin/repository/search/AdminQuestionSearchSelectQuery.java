package site.hospital.question.admin.repository.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AdminQuestionSearchSelectQuery {

    private Long questionId;
    private String memberIdName;
    private String nickName;
    private String hospitalName;
    private String content;
    private Long answerId;
    private String answerContent;

    @QueryProjection
    public AdminQuestionSearchSelectQuery(
            Long questionId,
            String memberIdName,
            String nickName,
            String hospitalName,
            String content,
            Long answerId,
            String answerContent
    ) {
        this.questionId = questionId;
        this.memberIdName = memberIdName;
        this.nickName = nickName;
        this.hospitalName = hospitalName;
        this.content = content;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }
}
