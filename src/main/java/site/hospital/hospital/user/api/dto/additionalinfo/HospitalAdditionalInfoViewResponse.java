package site.hospital.hospital.user.api.dto.additionalinfo;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.hospital.user.domain.StaffHosInformation;

@Data
public class HospitalAdditionalInfoViewResponse {

    private final String introduction;
    private final String consultationHour;
    private final String abnormality;
    private final List<HospitalAdditionalInfoDoctorDTO> doctors;

    private HospitalAdditionalInfoViewResponse(StaffHosInformation hospitalAdditionalInfo) {
        this.introduction = hospitalAdditionalInfo.getIntroduction();
        this.consultationHour = hospitalAdditionalInfo.getConsultationHour();
        this.abnormality = hospitalAdditionalInfo.getAbnormality();
        this.doctors = hospitalAdditionalInfo
                .getDoctors()
                .stream()
                .map(d -> new HospitalAdditionalInfoDoctorDTO(d))
                .collect(Collectors.toList());
    }

    public static HospitalAdditionalInfoViewResponse from(StaffHosInformation hospitalAdditionalInfo) {
        Assert.notNull(hospitalAdditionalInfo,"hospitalAdditionalInfo must be provided");

        return new HospitalAdditionalInfoViewResponse(hospitalAdditionalInfo);
    }
}
