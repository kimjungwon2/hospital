package site.hospital.repository.hospital.searchQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PostTagDto {
    private Long hospitalId;
    private String tagName;

    @QueryProjection
    public PostTagDto(Long hospitalId, String tagName) {
        this.hospitalId = hospitalId;
        this.tagName = tagName;
    }
}
