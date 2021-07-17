package site.hospital.repository.hospital.query;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class HospitalSearchDto {

    private Long hospitalId;
    private String hospitalName;
    private String businessCondition;
    private String medicalSubjectInformation;
    private String roadBaseAddress;
    private List<PostTagDto> postTagDtos;
    private List<ReviewHospitalDto> reviewHospitals;

    @QueryProjection
    public HospitalSearchDto(Long hospitalId, String hospitalName, String businessCondition,
                             String medicalSubjectInformation,
                             String roadBaseAddress) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.businessCondition = businessCondition;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.roadBaseAddress = roadBaseAddress;
    }
}
