package site.hospital.estimation.api.dto;

import lombok.Data;
import site.hospital.estimation.domain.EstimationList;

@Data
public class EstimationAdminModifyRequest {

    private String distinctionGrade;
    private EstimationList estimationList;

}
