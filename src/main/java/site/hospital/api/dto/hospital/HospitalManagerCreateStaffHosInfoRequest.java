package site.hospital.api.dto.hospital;

import java.util.List;
import lombok.Data;
import site.hospital.dto.doctor.CreateDoctorRequest;

@Data
public class HospitalManagerCreateStaffHosInfoRequest {

    private Long hospitalId;
    private Long memberId;
    private String introduction;
    private String consultationHour;
    private String abnormality;
    private List<CreateDoctorRequest> doctors;
}
