package site.hospital.repository.hospital.searchQuery;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.hospital.BusinessCondition;

import java.util.List;

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
    public HospitalSearchDto(Long hospitalId, String hospitalName, String imageKey, BusinessCondition businessCondition,
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
