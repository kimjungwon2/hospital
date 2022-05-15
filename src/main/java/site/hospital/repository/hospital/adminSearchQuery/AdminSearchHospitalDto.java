package site.hospital.repository.hospital.adminSearchQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.hospital.BusinessCondition;

@Data
public class AdminSearchHospitalDto {
    Long hospitalId;
    Long detailedHosId;
    Long staffHosInformationId;
    Long thumbnailId;
    String hospitalName;
    BusinessCondition businessCondition;
    String cityName;
    String phoneNumber;

    @QueryProjection
    public AdminSearchHospitalDto(Long hospitalId, Long detailedHosId, Long staffHosInformationId,
                                  Long thumbnailId,
                                  String hospitalName, BusinessCondition businessCondition,
                                  String cityName, String phoneNumber) {
        this.hospitalId = hospitalId;
        this.detailedHosId = detailedHosId;
        this.staffHosInformationId = staffHosInformationId;
        this.thumbnailId = thumbnailId;
        this.hospitalName = hospitalName;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
        this.phoneNumber = phoneNumber;
    }
}
