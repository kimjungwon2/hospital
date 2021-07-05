package site.hospital.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import site.hospital.domain.HospitalAddress;
import site.hospital.domain.HospitalLocation;

@Data
public class CreateHospitalRequest {
    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubject;
    private String medicalSubjectInformation;
    private String businessCondition;
    private String cityName;

    private int numberHealthcareProvider;
    private int numberWard;
    //입원실 수
    private int numberPatientRoom;

    private HospitalAddress hospitalAddress;
    private HospitalLocation hospitalLocation;
}
