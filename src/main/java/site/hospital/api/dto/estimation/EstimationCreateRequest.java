package site.hospital.api.dto.estimation;

import javax.validation.constraints.NotNull;
import lombok.Data;
import site.hospital.domain.estimation.EstimationList;

@Data
public class EstimationCreateRequest {

    @NotNull(message = "병원 번호를 입력해주세요.")
    private Long hospitalId;
    @NotNull(message = "도시 이름을 입력해주세요.")
    private String cityName;
    @NotNull(message = "병원 이름을 입력해주세요.")
    private String hospitalName;
    @NotNull(message = "등급을 입력해주세요.")
    private String distinctionGrade;
    @NotNull(message = "평가 내역을 입력해주세요.")
    private EstimationList estimationList;
}
