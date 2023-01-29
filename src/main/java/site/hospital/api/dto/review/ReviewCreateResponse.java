package site.hospital.api.dto.review;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ReviewCreateResponse {

    private final Long id;

    public static ReviewCreateResponse from(Long id) {
        return ReviewCreateResponse
                .builder()
                .id(id)
                .build();
    }
}
