package site.hospital.question.user.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.question.user.domain.Question;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class QuestionSearchResponse {

    private final Long questionId;
    private final String memberIdName;
    private final String nickName;
    private final String content;
    private final Long answerId;
    private final String answerContent;

    public static QuestionSearchResponse from(Question question) {
        Assert.notNull(question.getMember(),"member must be provided");

        if (question.getAnswer() != null) {
            return QuestionSearchResponse
                    .builder()
                    .memberIdName(question.getMember().getMemberIdName())
                    .questionId(question.getId())
                    .nickName(question.getMember().getNickName())
                    .content(question.getContent())
                    .answerId(question.getAnswer().getId())
                    .answerContent(question.getAnswer().getAnswerContent())
                    .build();
        }

        return QuestionSearchResponse
                .builder()
                .memberIdName(question.getMember().getMemberIdName())
                .questionId(question.getId())
                .nickName(question.getMember().getNickName())
                .content(question.getContent())
                .build();

    }
}
