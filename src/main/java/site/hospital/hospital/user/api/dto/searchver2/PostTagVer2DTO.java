package site.hospital.hospital.user.api.dto.searchver2;

import lombok.Data;
import site.hospital.tag.manager.domain.PostTag;

@Data
public class PostTagVer2DTO {

    private Long tagId;
    private String tagName;

    public PostTagVer2DTO(PostTag postTag) {
        this.tagId = postTag.getTag().getId();
        this.tagName = postTag.getTag().getName();
    }

}
