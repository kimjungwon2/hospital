package site.hospital.api.dto.postTag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.PostTag;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class PostTagViewHospitalTagResponse {

    private final String tagName;
    private final Long tagId;

    public static PostTagViewHospitalTagResponse from(PostTag postTag) {
        return PostTagViewHospitalTagResponse
                .builder()
                .tagName(postTag.getTag().getName())
                .tagId(postTag.getTag().getId())
                .build();
    }
}
