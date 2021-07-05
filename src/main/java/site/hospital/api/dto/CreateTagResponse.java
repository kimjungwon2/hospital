package site.hospital.api.dto;

import lombok.Data;

@Data
public class CreateTagResponse {
    private long tagId;

    public CreateTagResponse(long tagId) {
        this.tagId = tagId;
    }
}
