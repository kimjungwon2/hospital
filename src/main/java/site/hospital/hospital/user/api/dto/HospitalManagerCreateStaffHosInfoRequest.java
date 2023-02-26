package site.hospital.hospital.user.api.dto;

import java.util.List;
import lombok.Data;
import site.hospital.doctor.admin.api.dto.DoctorAdminCreateRequest;

@Data
public class HospitalManagerCreateStaffHosInfoRequest {

    private Long hospitalId;
    private Long memberId;
    private String introduction;
    private String consultationHour;
    private String abnormality;
    private List<DoctorAdminCreateRequest> doctors;
}
