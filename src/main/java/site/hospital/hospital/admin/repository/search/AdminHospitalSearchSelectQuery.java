package site.hospital.hospital.admin.repository.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class AdminHospitalSearchSelectQuery {

    Long hospitalId;
    Long detailedHosId;
    Long staffHosInformationId;
    Long thumbnailId;
    String hospitalName;
    BusinessCondition businessCondition;
    String cityName;
    String phoneNumber;

    @QueryProjection
    public AdminHospitalSearchSelectQuery(
            Long hospitalId,
            Long detailedHosId,
            Long staffHosInformationId,
            Long thumbnailId,
            String hospitalName,
            BusinessCondition businessCondition,
            String cityName,
            String phoneNumber
    ) {
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
