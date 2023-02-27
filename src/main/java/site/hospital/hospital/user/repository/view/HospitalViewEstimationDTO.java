package site.hospital.hospital.user.repository.view;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.estimation.user.domain.EstimationList;

@Data
public class HospitalViewEstimationDTO {

    private Long hospitalId;
    private String distinctionGrade;
    private EstimationList estimationList;

    @QueryProjection
    public HospitalViewEstimationDTO(Long hospitalId, String distinctionGrade,
            EstimationList estimationList) {
        this.hospitalId = hospitalId;
        this.distinctionGrade = distinctionGrade;
        this.estimationList = estimationList;
    }
}
