package site.hospital.hospital.admin.repository.dto.view;

import lombok.Data;
import site.hospital.tag.manager.domain.PostTag;

@Data
public class AdminHospitalTagDTO {

    private Long postTagId;
    private String tagName;

    public AdminHospitalTagDTO(PostTag postTag) {
        this.postTagId = postTag.getId();
        this.tagName = postTag.getTag().getName();
    }
}
