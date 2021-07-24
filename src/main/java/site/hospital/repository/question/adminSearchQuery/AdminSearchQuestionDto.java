package site.hospital.repository.question.adminSearchQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AdminSearchQuestionDto {
    private Long questionId;
    private String memberIdName;
    private String nickName;
    private String hospitalName;
    private String content;
    private String answerContent;

    @QueryProjection
    public AdminSearchQuestionDto(Long questionId, String memberIdName, String nickName, String hospitalName, String content, String answerContent) {
        this.questionId = questionId;
        this.memberIdName = memberIdName;
        this.nickName = nickName;
        this.hospitalName = hospitalName;
        this.content = content;
        this.answerContent = answerContent;
    }
}
