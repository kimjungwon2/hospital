package site.hospital.tag.manager.api.dto.posttag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.tag.manager.domain.PostTag;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class PostTagViewHospitalTagResponse {

    private final String tagName;
    private final Long tagId;

    public static PostTagViewHospitalTagResponse from(PostTag postTag) {
        Assert.notNull(postTag.getTag(),"tag must be provided");
        return PostTagViewHospitalTagResponse
                .builder()
                .tagName(postTag.getTag().getName())
                .tagId(postTag.getTag().getId())
                .build();
    }
}
