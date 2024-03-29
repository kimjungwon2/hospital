package site.hospital.tag.manager.api.dto.posttag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class PostTagLinkTagResponse {

    private final Long postTagId;

    public static PostTagLinkTagResponse from(long postTagId) {
        return PostTagLinkTagResponse
                .builder()
                .postTagId(postTagId)
                .build();

    }

}
