package site.hospital.hospital.admin.repository.dto;

import lombok.Data;
import site.hospital.tag.domain.PostTag;

@Data
public class AdminHospitalTagDTO {

    private Long postTagId;
    private String tagName;

    public AdminHospitalTagDTO(PostTag postTag) {
        this.postTagId = postTag.getId();
        this.tagName = postTag.getTag().getName();
    }
}
