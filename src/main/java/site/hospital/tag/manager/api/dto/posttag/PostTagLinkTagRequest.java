package site.hospital.tag.manager.api.dto.posttag;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostTagLinkTagRequest {

    @NotNull(message = "태그 번호를 입력해주세요.")
    private Long tagId;
    @NotNull(message = "병원 번호를 입력해주세요.")
    private Long hospitalId;
}
