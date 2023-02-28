package site.hospital.hospital.user.repository.view;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class HospitalViewSelectQuery {

    private Long hospitalId;
    private Long detailedHosId;
    private Long staffHosInfoId;
    private Long hospitalThumbnailId;
    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubjectInformation;
    private BusinessCondition businessCondition;
    private String cityName;

    private String landLotBasedSystem;
    private String roadBaseAddress;
    private String zipCode;

    private Integer numberHealthcareProvider;
    private Integer numberWard;
    private Integer numberPatientRoom;

    private BigDecimal x_coordination;
    private BigDecimal y_coordination;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private Long hospitalReviewCount;
    private List<HospitalViewTagDTO> hospitalTags;
    private List<HospitalViewEstimationDTO> hospitalEstimations;
    private List<HospitalViewImagesDTO> hospitalImages;

    @QueryProjection
    public HospitalViewSelectQuery(
            Long hospitalId,
            Long detailedHosId,
            Long staffHosInfoId,
            Long hospitalThumbnailId,
            String licensingDate,
            String hospitalName,
            String phoneNumber,
            String distinguishedName,
            String medicalSubjectInformation,
            BusinessCondition businessCondition,
            String cityName,
            String landLotBasedSystem,
            String zipCode,
            String roadBaseAddress,
            Integer numberHealthcareProvider,
            Integer numberWard,
            Integer numberPatientRoom,
            BigDecimal x_coordination,
            BigDecimal y_coordination,
            BigDecimal latitude,
            BigDecimal longitude
    ) {

        this.hospitalId = hospitalId;
        this.detailedHosId = detailedHosId;
        this.staffHosInfoId = staffHosInfoId;
        this.hospitalThumbnailId = hospitalThumbnailId;
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;

        this.landLotBasedSystem = landLotBasedSystem;
        this.roadBaseAddress = roadBaseAddress;
        this.zipCode = zipCode;
        this.numberHealthcareProvider = numberHealthcareProvider;
        this.numberWard = numberWard;
        this.numberPatientRoom = numberPatientRoom;
        this.x_coordination = x_coordination;
        this.y_coordination = y_coordination;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
