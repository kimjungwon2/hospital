package site.hospital.api.dto.question;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.Question;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class QuestionSearchResponse {

    private Long questionId;
    private String memberIdName;
    private String nickName;
    private String content;
    private Long answerId;
    private String answerContent;

    public static QuestionSearchResponse from(Question question) {

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
