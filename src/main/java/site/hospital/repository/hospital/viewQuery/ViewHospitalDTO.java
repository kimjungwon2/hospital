package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.hospital.BusinessCondition;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ViewHospitalDTO {
    private Long hospitalId;
    private Long detailedHosId;
    private Long staffHosInfoId;
    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubjectInformation;
    private BusinessCondition businessCondition;
    private String cityName;


    /*상세 정보*/
    //주소
    private String landLotBasedSystem;
    private String roadBaseAddress;

    private Integer numberHealthcareProvider;
    private Integer numberWard;
    private Integer numberPatientRoom;

    // 지도 위치.
    private BigDecimal x_coordination;
    private BigDecimal y_coordination;
    //위도, 경도
    private BigDecimal latitude;
    private BigDecimal longitude;

    private List<HospitalReviewDTO> hospitalReviews;
    private List<HospitalTagDTO> hospitalTags;
    private List<HospitalEstimationDTO> hospitalEstimations;

    @QueryProjection
    public ViewHospitalDTO(Long hospitalId, Long detailedHosId, Long staffHosInfoId, String licensingDate, String hospitalName,
                           String phoneNumber, String distinguishedName,
                           String medicalSubjectInformation, BusinessCondition businessCondition,
                           String cityName, String landLotBasedSystem,
                           String roadBaseAddress, Integer numberHealthcareProvider,
                           Integer numberWard, Integer numberPatientRoom,
                           BigDecimal x_coordination, BigDecimal y_coordination,
                           BigDecimal latitude, BigDecimal longitude) {

        this.hospitalId = hospitalId;
        this.detailedHosId = detailedHosId;
        this.staffHosInfoId = staffHosInfoId;
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;

        this.landLotBasedSystem = landLotBasedSystem;
        this.roadBaseAddress = roadBaseAddress;
        this.numberHealthcareProvider = numberHealthcareProvider;
        this.numberWard = numberWard;
        this.numberPatientRoom = numberPatientRoom;
        this.x_coordination = x_coordination;
        this.y_coordination = y_coordination;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
