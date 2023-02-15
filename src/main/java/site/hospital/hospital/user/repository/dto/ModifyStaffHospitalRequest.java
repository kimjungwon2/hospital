package site.hospital.hospital.user.repository.dto;

import lombok.Data;

@Data
public class ModifyStaffHospitalRequest {

    Long staffHosInformationId;
    String introduction;
    String consultationHour;
    String abnormality;
}
