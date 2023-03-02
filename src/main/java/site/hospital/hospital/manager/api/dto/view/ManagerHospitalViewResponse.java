package site.hospital.hospital.manager.api.dto.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.hospital.user.domain.BusinessCondition;
import site.hospital.hospital.user.domain.Hospital;

@Data
public class ManagerHospitalViewResponse {

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
    private BigDecimal xCoordination;
    private BigDecimal yCoordination;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private Long staffHosInfoId;
    private Long thumbnailId;

    private List<ManagerHospitalViewTagDTO> hospitalTags;
    private List<ManagerHospitalViewEstimationDTO> estimations;

    private ManagerHospitalViewResponse(
            Hospital hospital,
            Long detailedHosInfoId,
            Long staffHosInfoId,
            Long hospitalThumbnailId
    ) {
        this.hospitalId = hospital.getId();
        this.licensingDate = hospital.getLicensingDate();
        this.hospitalName = hospital.getHospitalName();
        this.phoneNumber = hospital.getPhoneNumber();
        this.distinguishedName = hospital.getDistinguishedName();
        this.medicalSubjectInformation = hospital.getMedicalSubjectInformation();
        this.businessCondition = hospital.getBusinessCondition();
        this.cityName = hospital.getCityName();

        if (detailedHosInfoId != null) {
            Assert.notNull(hospital.getDetailedHosInformation(),"detailedHosInfo must be provided");

            this.detailedHosInfoId = hospital.getDetailedHosInformation().getId();
            this.numberHealthcareProvider = hospital.getDetailedHosInformation()
                    .getNumberHealthcareProvider();
            this.numberWard = hospital.getDetailedHosInformation().getNumberWard();
            this.numberPatientRoom = hospital.getDetailedHosInformation().getNumberPatientRoom();
            this.landLotBasedSystem = hospital.getDetailedHosInformation().getHospitalAddress()
                    .getLandLotBasedSystem();
            this.roadBaseAddress = hospital.getDetailedHosInformation().getHospitalAddress()
                    .getRoadBaseAddress();
            this.zipCode = hospital.getDetailedHosInformation().getHospitalAddress().getZipCode();
            this.xCoordination = hospital.getDetailedHosInformation().getHospitalLocation()
                    .getXCoordination();
            this.yCoordination = hospital.getDetailedHosInformation().getHospitalLocation()
                    .getYCoordination();
            this.latitude = hospital.getDetailedHosInformation().getHospitalLocation()
                    .getLatitude();
            this.longitude = hospital.getDetailedHosInformation().getHospitalLocation()
                    .getLongitude();
        }

        if (staffHosInfoId != null) {
            Assert.notNull(hospital.getStaffHosInformation(),"hosAdditionalInfo must be provided");

            this.staffHosInfoId = hospital.getStaffHosInformation().getId();
        }

        if (hospitalThumbnailId != null) {
            Assert.notNull(hospital.getHospitalThumbnail(),"thumbnail must be provided");

            this.thumbnailId = hospital.getHospitalThumbnail().getId();
        }

        this.hospitalTags = hospital
                .getPostTags()
                .stream()
                .map(ManagerHospitalViewTagDTO::new)
                .collect(Collectors.toList());

        this.estimations = hospital
                .getEstimations()
                .stream()
                .map(ManagerHospitalViewEstimationDTO::new)
                .collect(Collectors.toList());
    }

    public static ManagerHospitalViewResponse from(
            Hospital hospital,
            Long detailedHosInfoId,
            Long staffHosInfoId,
            Long hospitalThumbnailId
    ){
        Assert.notNull(hospital,"hospital must be provided");

        return new ManagerHospitalViewResponse(hospital,detailedHosInfoId, staffHosInfoId, hospitalThumbnailId);
    }
}
