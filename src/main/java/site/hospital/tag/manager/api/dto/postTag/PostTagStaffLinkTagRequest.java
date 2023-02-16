package site.hospital.tag.manager.api.dto.postTag;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostTagStaffLinkTagRequest {

    @NotNull(message = "회원 번호를 입력해주세요.")
    private Long memberId;
    @NotNull(message = "태그 번호를 입력해주세요.")
    private Long tagId;
    @NotNull(message = "병원 번호를 입력해주세요.")
    private Long hospitalId;
}
