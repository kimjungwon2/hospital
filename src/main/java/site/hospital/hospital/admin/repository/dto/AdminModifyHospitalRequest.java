package site.hospital.hospital.admin.repository.dto;

import lombok.Data;
import site.hospital.hospital.user.domain.detailedHosInformation.HospitalAddress;
import site.hospital.hospital.user.domain.detailedHosInformation.HospitalLocation;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class AdminModifyHospitalRequest {

    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubjectInformation;
    private BusinessCondition businessCondition;
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
