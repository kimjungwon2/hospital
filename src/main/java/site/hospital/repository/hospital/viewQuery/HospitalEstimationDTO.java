package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.estimation.EstimationList;

@Data
public class HospitalEstimationDTO {

    private Long hospitalId;
    private String distinctionGrade;
    private EstimationList estimationList;

    @QueryProjection
    public HospitalEstimationDTO(Long hospitalId, String distinctionGrade,
            EstimationList estimationList) {
        this.hospitalId = hospitalId;
        this.distinctionGrade = distinctionGrade;
        this.estimationList = estimationList;
    }
}
