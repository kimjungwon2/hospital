package site.hospital.api.dto.staffHospital;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.StaffHosInformation;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class StaffHospitalViewResponse {

    private String introduction;
    private String consultationHour;
    private String abnormality;
    private List<StaffHospitalDoctorDTO> doctors;

    public static StaffHospitalViewResponse from(StaffHosInformation staffHosInformation) {
        return StaffHospitalViewResponse
                .builder()
                .introduction(staffHosInformation.getIntroduction())
                .consultationHour(staffHosInformation.getConsultationHour())
                .abnormality(staffHosInformation.getAbnormality())
                .doctors(staffHosInformation.getDoctors().stream()
                        .map(d -> new StaffHospitalDoctorDTO(d))
                        .collect(Collectors.toList()))
                .build();

    }
}
