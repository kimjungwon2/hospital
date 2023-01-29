package site.hospital.api.dto.estimation;

import lombok.Data;
import site.hospital.domain.estimation.EstimationList;

@Data
public class EstimationAdminModifyRequest {

    private String distinctionGrade;
    private EstimationList estimationList;

}
