package site.hospital.dto;

import lombok.Data;
import site.hospital.domain.detailedHosInformation.HospitalAddress;
import site.hospital.domain.detailedHosInformation.HospitalLocation;

@Data
public class AdminModifyHospitalRequest {
    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubjectInformation;
    private String businessCondition;
    private String cityName;

    //병원 상세 정보
    private Boolean detailedModifyCheck;
    private Long detailedHosInfoId;
    private Integer numberHealthcareProvider;
    private Integer numberWard;
    private Integer numberPatientRoom;
    private HospitalAddress hospitalAddress;
    private HospitalLocation hospitalLocation;
}
