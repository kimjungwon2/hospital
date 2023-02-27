package site.hospital.hospital.user.repository.search;


import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Data;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class HospitalSearchSelectQuery {

    private Long hospitalId;
    private String hospitalName;
    private String imageKey;
    private BusinessCondition businessCondition;
    private String medicalSubjectInformation;
    private String roadBaseAddress;
    private List<HospitalSearchPostTagDTO> hospitalSearchPostTagDTOS;
    private List<HospitalSearchReviewHospitalDTO> reviewHospitals;

    @QueryProjection
    public HospitalSearchSelectQuery(Long hospitalId, String hospitalName, String imageKey,
            BusinessCondition businessCondition,
            String medicalSubjectInformation,
            String roadBaseAddress) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.imageKey = imageKey;
        this.businessCondition = businessCondition;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.roadBaseAddress = roadBaseAddress;
    }
}
