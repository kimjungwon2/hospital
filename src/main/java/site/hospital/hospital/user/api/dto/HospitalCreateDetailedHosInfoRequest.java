package site.hospital.hospital.user.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import site.hospital.hospital.user.domain.detailedinfo.HospitalAddress;
import site.hospital.hospital.user.domain.detailedinfo.HospitalLocation;

@Data
public class HospitalCreateDetailedHosInfoRequest {

    @NotNull(message = "병원 번호를 입력해주세요.")
    private Long hospitalId;
    @NotNull(message = "종업원 수를 입력해주세요.")
    private Integer numberHealthcareProvider;
    @NotNull(message = "병실 수를 입력해주세요.")
    private Integer numberWard;
    @NotNull(message = "환자실 수를 입력해주세요.")
    private Integer numberPatientRoom;
    @NotNull(message = "병원 위치 좌표를 입력해주세요.")
    private HospitalLocation hospitalLocation;
    @NotNull(message = "병원 주소를 입력해주세요.")
    private HospitalAddress hospitalAddress;

}
