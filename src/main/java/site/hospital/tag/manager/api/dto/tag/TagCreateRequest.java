package site.hospital.tag.manager.api.dto.tag;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TagCreateRequest {

    @NotNull(message = "태그 이름을 입력해주세요.")
    private String tagName;
}
