package site.hospital.answer.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class AnswerCreateResponse {

    private final Long answerId;

    public static AnswerCreateResponse from(Long answerId) {
        return AnswerCreateResponse
                .builder()
                .answerId(answerId)
                .build();
    }
}
