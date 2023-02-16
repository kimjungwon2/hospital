package site.hospital.tag.api.dto.tag;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.tag.domain.Tag;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class TagSearchTagnameResponse {

    private final Long tagId;
    private final String name;
    private final LocalDateTime createdDate;

    public static TagSearchTagnameResponse from(Tag tag) {
        return TagSearchTagnameResponse
                .builder()
                .tagId(tag.getId())
                .name(tag.getName())
                .createdDate(tag.getCreatedDate())
                .build();
    }
}
