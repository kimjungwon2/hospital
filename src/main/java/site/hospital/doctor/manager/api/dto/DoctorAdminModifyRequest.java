package site.hospital.doctor.manager.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorAdminModifyRequest {

    @NotNull(message = "의사 경력을 입력해주세요.")
    private String history;
    @NotNull(message = "의사 이름을 입력해주세요.")
    private String name;
}
