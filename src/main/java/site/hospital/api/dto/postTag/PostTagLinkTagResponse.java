package site.hospital.api.dto.postTag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class PostTagLinkTagResponse {

    private Long postTagId;

    public static PostTagLinkTagResponse from(long postTagId) {
        return PostTagLinkTagResponse
                .builder()
                .postTagId(postTagId)
                .build();

    }

}
