package site.hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminHospitalSearchCondition {
    Long hospitalId;
    String hospitalName;
    String businessCondition;
    String cityName;


    @Builder
    public AdminHospitalSearchCondition(Long hospitalId, String hospitalName,
                                        String businessCondition, String cityName) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
    }
}
