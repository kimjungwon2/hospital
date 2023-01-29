package site.hospital.api.dto.hospital;

import lombok.Data;
import site.hospital.domain.detailedHosInformation.HospitalAddress;
import site.hospital.domain.detailedHosInformation.HospitalLocation;

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
