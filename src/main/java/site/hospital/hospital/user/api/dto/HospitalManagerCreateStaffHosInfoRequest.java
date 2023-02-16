package site.hospital.hospital.user.api.dto;

import java.util.List;
import lombok.Data;
import site.hospital.doctor.manager.repository.dto.CreateDoctorRequest;

@Data
public class HospitalManagerCreateStaffHosInfoRequest {

    private Long hospitalId;
    private Long memberId;
    private String introduction;
    private String consultationHour;
    private String abnormality;
    private List<CreateDoctorRequest> doctors;
}
