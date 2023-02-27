package site.hospital.hospital.manager.api.dto;


import lombok.Data;
import site.hospital.hospital.user.domain.detailedinfo.HospitalAddress;
import site.hospital.hospital.user.domain.detailedinfo.HospitalLocation;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class ManagerModifyHospitalRequest {

    private Long memberId;
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
