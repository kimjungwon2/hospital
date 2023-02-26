package site.hospital.hospital.admin.repository.dto;

import lombok.Data;
import site.hospital.estimation.user.domain.Estimation;
import site.hospital.estimation.user.domain.EstimationList;

@Data
public class AdminHospitalEstimationDTO {

    private Long estimationId;

    private String city;
    private String hospitalName;
    private String distinctionGrade;
    private EstimationList estimationList;

    public AdminHospitalEstimationDTO(Estimation estimation) {
        this.estimationId = estimation.getId();
        this.city = estimation.getCityName();
        this.hospitalName = estimation.getHospitalName();
        this.distinctionGrade = estimation.getDistinctionGrade();
        this.estimationList = estimation.getEstimationList();
    }
}
