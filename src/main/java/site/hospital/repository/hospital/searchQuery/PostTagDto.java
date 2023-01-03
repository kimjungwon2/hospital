package site.hospital.repository.hospital.searchQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PostTagDto {

    private Long hospitalId;
    private Long tagId;
    private String tagName;

    @QueryProjection
    public PostTagDto(Long hospitalId, Long tagId, String tagName) {
        this.hospitalId = hospitalId;
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
