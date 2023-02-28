package site.hospital.hospital.manager.api.dto.view;

import lombok.Data;
import site.hospital.estimation.user.domain.Estimation;
import site.hospital.estimation.user.domain.EstimationList;

@Data
public class ManagerHospitalViewEstimationDTO {

    private Long estimationId;

    private String city;
    private String hospitalName;
    private String distinctionGrade;
    private EstimationList estimationList;

    public ManagerHospitalViewEstimationDTO(Estimation estimation) {
        this.estimationId = estimation.getId();
        this.city = estimation.getCityName();
        this.hospitalName = estimation.getHospitalName();
        this.distinctionGrade = estimation.getDistinctionGrade();
        this.estimationList = estimation.getEstimationList();
    }
}
