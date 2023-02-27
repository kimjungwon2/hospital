package site.hospital.hospital.user.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import site.hospital.hospital.user.domain.detailedinfo.HospitalAddress;
import site.hospital.hospital.user.domain.detailedinfo.HospitalLocation;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class HospitalCreateRequest {

    @NotNull(message = "개업일을 입력해주세요..")
    private String licensingDate;
    @NotNull(message = "병원 이름을 입력해주세요.")
    private String hospitalName;
    @NotNull(message = "전화번호를 입력해주세요.")
    private String phoneNumber;
    @NotNull(message = "병원 종류를 입력해주세요.")
    private String distinguishedName;
    @NotNull(message = "진료 과목을 입력해주세요.")
    private String medicalSubjectInformation;
    @NotNull(message = "영업 상태를 입력해주세요.")
    private BusinessCondition businessCondition;
    @NotNull(message = "도시 이름을 입력해주세요.")
    private String cityName;

    @NotNull(message = "상세정보 등록 유무를 체크해주세요.")
    private Boolean detailedInfoCheck;

    private Integer numberHealthcareProvider;
    private Integer numberWard;
    private Integer numberPatientRoom;

    private HospitalLocation hospitalLocation;
    private HospitalAddress hospitalAddress;
}
