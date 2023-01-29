package site.hospital.api.dto.tag;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.Tag;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class TagAdminViewAllResponse {

    private final Long tagId;
    private final String name;
    private final LocalDateTime createdDate;

    public static TagAdminViewAllResponse from(Tag tag) {
        return TagAdminViewAllResponse
                .builder()
                .tagId(tag.getId())
                .name(tag.getName())
                .createdDate(tag.getCreatedDate())
                .build();

    }
}
