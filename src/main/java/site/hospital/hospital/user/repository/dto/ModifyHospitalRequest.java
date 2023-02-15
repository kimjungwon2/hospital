package site.hospital.hospital.user.repository.dto;

import lombok.Data;
import site.hospital.hospital.user.domain.detailedHosInformation.HospitalAddress;
import site.hospital.hospital.user.domain.detailedHosInformation.HospitalLocation;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class ModifyHospitalRequest {

    private Long hospitalId;
    private Long detailedHosInformationId;
    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubjectInformation;
    private BusinessCondition businessCondition;
    private String cityName;

    private int numberHealthcareProvider;
    private int numberWard;
    //입원실 수
    private int numberPatientRoom;

    private HospitalAddress hospitalAddress;
    private HospitalLocation hospitalLocation;
}
