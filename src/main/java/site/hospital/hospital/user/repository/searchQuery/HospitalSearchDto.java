package site.hospital.hospital.user.repository.searchQuery;


import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Data;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
public class HospitalSearchDto {

    private Long hospitalId;
    private String hospitalName;
    private String imageKey;
    private BusinessCondition businessCondition;
    private String medicalSubjectInformation;
    private String roadBaseAddress;
    private List<PostTagDto> postTagDtos;
    private List<ReviewHospitalDto> reviewHospitals;

    @QueryProjection
    public HospitalSearchDto(Long hospitalId, String hospitalName, String imageKey,
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
