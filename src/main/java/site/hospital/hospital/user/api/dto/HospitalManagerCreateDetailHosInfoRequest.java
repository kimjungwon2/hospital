package site.hospital.hospital.user.api.dto;

import lombok.Data;
import site.hospital.hospital.user.domain.detailedinfo.HospitalAddress;
import site.hospital.hospital.user.domain.detailedinfo.HospitalLocation;

@Data
public class HospitalManagerCreateDetailHosInfoRequest {

    private Long hospitalId;
    private Long memberId;
    private Integer numberHealthcareProvider;
    private Integer numberWard;
    private Integer numberPatientRoom;

    private HospitalLocation hospitalLocation;
    private HospitalAddress hospitalAddress;

}
