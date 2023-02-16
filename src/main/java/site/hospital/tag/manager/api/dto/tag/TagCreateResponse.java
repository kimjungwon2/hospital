package site.hospital.tag.manager.api.dto.tag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

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
