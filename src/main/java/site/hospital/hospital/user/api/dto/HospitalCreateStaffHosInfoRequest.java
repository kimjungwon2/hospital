package site.hospital.hospital.user.api.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import site.hospital.doctor.admin.api.dto.DoctorAdminCreateRequest;

@Data
public class HospitalCreateStaffHosInfoRequest {

    @NotNull(message = "병원 번호를 입력해주세요.")
    private Long hospitalId;
    @NotNull(message = "병원 소개를 입력해주세요.")
    private String introduction;
    @NotNull(message = "영업 시간을 입력해주세요.")
    private String consultationHour;
    @NotNull(message = "특이사항을 입력해주세요.")
    private String abnormality;
    private List<DoctorAdminCreateRequest> doctors;
}
