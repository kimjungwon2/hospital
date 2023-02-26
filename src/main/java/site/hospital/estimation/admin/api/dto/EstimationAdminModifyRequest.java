package site.hospital.estimation.admin.api.dto;

import lombok.Data;
import site.hospital.estimation.user.domain.EstimationList;

@Data
public class EstimationAdminModifyRequest {

    private String distinctionGrade;
    private EstimationList estimationList;

}
