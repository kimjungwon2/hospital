package site.hospital.api.dto.tag;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.Tag;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class TagAdminViewAllResponse {

    private Long tagId;
    private String name;
    private LocalDateTime createdDate;

    public static TagAdminViewAllResponse from(Tag tag) {
        return TagAdminViewAllResponse
                .builder()
                .tagId(tag.getId())
                .name(tag.getName())
                .createdDate(tag.getCreatedDate())
                .build();

    }
}
