package site.hospital.dto.hospital.staff;

import lombok.Data;
import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.domain.hospital.Hospital;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class StaffHospitalView {

    private Long hospitalId;

    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubjectInformation;
    private BusinessCondition businessCondition;
    private String cityName;

    private Long detailedHosInfoId;

    private Integer numberHealthcareProvider;
    private Integer numberWard;
    private Integer numberPatientRoom;
    private String landLotBasedSystem;
    private String roadBaseAddress;
    private String zipCode;
    private BigDecimal x_coordination;
    private BigDecimal y_coordination;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private Long staffHosInfoId;

    private List<StaffHospitalTagDTO> hospitalTags;
    private List<StaffHospitalEstimationDTO> estimations;

    public StaffHospitalView(Hospital hospital, Long detailedHosInfoId, Long staffHosInfoId) {
        this.hospitalId = hospital.getId();
        this.licensingDate = hospital.getLicensingDate();
        this.hospitalName = hospital.getHospitalName();
        this.phoneNumber = hospital.getPhoneNumber();
        this.distinguishedName = hospital.getDistinguishedName();
        this.medicalSubjectInformation = hospital.getMedicalSubjectInformation();
        this.businessCondition = hospital.getBusinessCondition();
        this.cityName = hospital.getCityName();

        if(detailedHosInfoId != null) {
            this.detailedHosInfoId = hospital.getDetailedHosInformation().getId();
            this.numberHealthcareProvider = hospital.getDetailedHosInformation().getNumberHealthcareProvider();;
            this.numberWard = hospital.getDetailedHosInformation().getNumberWard();;
            this.numberPatientRoom = hospital.getDetailedHosInformation().getNumberPatientRoom();;
            this.landLotBasedSystem = hospital.getDetailedHosInformation().getHospitalAddress().getLandLotBasedSystem();
            this.roadBaseAddress = hospital.getDetailedHosInformation().getHospitalAddress().getRoadBaseAddress();
            this.zipCode = hospital.getDetailedHosInformation().getHospitalAddress().getZipCode();
            this.x_coordination = hospital.getDetailedHosInformation().getHospitalLocation().getX_coordination();
            this.y_coordination = hospital.getDetailedHosInformation().getHospitalLocation().getY_coordination();
            this.latitude = hospital.getDetailedHosInformation().getHospitalLocation().getLatitude();
            this.longitude = hospital.getDetailedHosInformation().getHospitalLocation().getLongitude();
        }

        if(staffHosInfoId != null) {
            this.staffHosInfoId = hospital.getStaffHosInformation().getId();
        }

        this.hospitalTags = hospital.getPostTags().stream().map(h-> new StaffHospitalTagDTO(h))
                .collect(Collectors.toList());
        this.estimations = hospital.getEstimations().stream().map(e->new StaffHospitalEstimationDTO(e))
                .collect(Collectors.toList());
    }
}
