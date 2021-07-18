package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalEstimationDTO {
    private Long hospitalId;
    private String distinctionGrade;
    private String estimationList;

    @QueryProjection
    public HospitalEstimationDTO(Long hospitalId, String distinctionGrade, String estimationList) {
        this.hospitalId = hospitalId;
        this.distinctionGrade = distinctionGrade;
        this.estimationList = estimationList;
    }
}
