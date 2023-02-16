package site.hospital.question.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class QuestionCreateResponse {

    private final Long id;

    public static QuestionCreateResponse from(long id) {
        return QuestionCreateResponse
                .builder()
                .id(id)
                .build();
    }
}
