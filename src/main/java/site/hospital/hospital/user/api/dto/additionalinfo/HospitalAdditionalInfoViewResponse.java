package site.hospital.hospital.user.api.dto.additionalinfo;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.hospital.user.domain.StaffHosInformation;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class HospitalAdditionalInfoViewResponse {

    private final String introduction;
    private final String consultationHour;
    private final String abnormality;
    private final List<HospitalAdditionalInfoDoctorDTO> doctors;

    public static HospitalAdditionalInfoViewResponse from(StaffHosInformation hospitalAdditionalInfo) {
        return HospitalAdditionalInfoViewResponse
                .builder()
                .introduction(hospitalAdditionalInfo.getIntroduction())
                .consultationHour(hospitalAdditionalInfo.getConsultationHour())
                .abnormality(hospitalAdditionalInfo.getAbnormality())
                .doctors(hospitalAdditionalInfo.getDoctors().stream()
                        .map(d -> new HospitalAdditionalInfoDoctorDTO(d))
                        .collect(Collectors.toList()))
                .build();
    }
}
