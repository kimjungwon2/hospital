package site.hospital.estimation.admin.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import site.hospital.estimation.user.domain.EstimationList;

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
