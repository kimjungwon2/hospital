package site.hospital.dto.hospital.admin;

import lombok.Data;
import site.hospital.domain.PostTag;

@Data
public class AdminHospitalTagDTO {
    private Long postTagId;
    private String tagName;

    public AdminHospitalTagDTO(PostTag postTag) {
        this.postTagId = postTag.getId();
        this.tagName = postTag.getTag().getName();
    }
}
