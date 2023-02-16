package site.hospital.question.repository.adminSearchQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AdminSearchQuestionDto {

    private Long questionId;
    private String memberIdName;
    private String nickName;
    private String hospitalName;
    private String content;
    private Long answerId;
    private String answerContent;

    @QueryProjection
    public AdminSearchQuestionDto(Long questionId, String memberIdName, String nickName,
            String hospitalName, String content,
            Long answerId, String answerContent) {
        this.questionId = questionId;
        this.memberIdName = memberIdName;
        this.nickName = nickName;
        this.hospitalName = hospitalName;
        this.content = content;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }
}
