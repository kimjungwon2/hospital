package site.hospital.api.dto.tag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.api.dto.estimation.EstimationCreateResponse;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class TagCreateResponse {

    private final Long tagId;

    public static TagCreateResponse from(Long tagId) {
        return TagCreateResponse
                .builder()
                .tagId(tagId)
                .build();
    }
}
