package site.hospital.api.dto.question;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class QuestionCreateResponse {

    private Long id;

    public static QuestionCreateResponse from(long id) {
        return QuestionCreateResponse
                .builder()
                .id(id)
                .build();
    }
}
