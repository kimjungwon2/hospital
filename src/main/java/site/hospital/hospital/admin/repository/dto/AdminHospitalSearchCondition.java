package site.hospital.hospital.admin.repository.dto;

import lombok.Builder;
import lombok.Data;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class AdminHospitalSearchCondition {

    Long hospitalId;
    String hospitalName;
    BusinessCondition businessCondition;
    String cityName;


    @Builder
    public AdminHospitalSearchCondition(Long hospitalId, String hospitalName,
            BusinessCondition businessCondition, String cityName) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
    }
}
