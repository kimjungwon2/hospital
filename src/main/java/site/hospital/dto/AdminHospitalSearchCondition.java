package site.hospital.dto;

import lombok.Builder;
import lombok.Data;
import site.hospital.domain.hospital.BusinessCondition;

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
