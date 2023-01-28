package site.hospital.api.dto.bookmark;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookmarkCreateRequest {

    @NotNull(message = "멤버 번호가 필요합니다.")
    private Long memberId;
    @NotNull(message = "병원 번호가 필요합니다.")
    private Long hospitalId;
}
