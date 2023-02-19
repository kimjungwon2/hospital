package site.hospital.hospital.user.api.dto.searchver2;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.hospital.user.domain.BusinessCondition;
import site.hospital.hospital.user.domain.Hospital;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class HospitalSearchListsVer2Response {

    private Long hospitalId;
    private String hospitalName;
    private String imageKey;
    private BusinessCondition businessCondition;
    private String medicalSubjectInformation;
    private String roadBaseAddress;
    private List<PostTagVer2DTO> postTagVer2DTOS;
    private List<ReviewHospitalVer2DTO> reviewHospitalVer2DTOS;

    public static HospitalSearchListsVer2Response from(Hospital hospital) {

        if(hospital.getHospitalThumbnail() == null) {
            return HospitalSearchListsVer2Response
                    .builder()
                    .hospitalId(hospital.getId())
                    .hospitalName(hospital.getHospitalName())
                    .imageKey(null)
                    .businessCondition(hospital.getBusinessCondition())
                    .medicalSubjectInformation(hospital.getMedicalSubjectInformation())
                    .roadBaseAddress(hospital.getDetailedHosInformation().getHospitalAddress()
                            .getRoadBaseAddress())
                    .postTagVer2DTOS(hospital.getPostTags().stream()
                            .map(postTag -> new PostTagVer2DTO(postTag))
                            .collect(Collectors.toList()))
                    .reviewHospitalVer2DTOS(hospital.getReviewHospitals().stream()
                            .map(reviewHospital -> new ReviewHospitalVer2DTO(reviewHospital))
                            .collect(Collectors.toList()))
                    .build();
        }
        else{
            return HospitalSearchListsVer2Response
                    .builder()
                    .hospitalId(hospital.getId())
                    .hospitalName(hospital.getHospitalName())
                    .imageKey(hospital.getHospitalThumbnail().getImageKey())
                    .businessCondition(hospital.getBusinessCondition())
                    .medicalSubjectInformation(hospital.getMedicalSubjectInformation())
                    .roadBaseAddress(hospital.getDetailedHosInformation().getHospitalAddress()
                            .getRoadBaseAddress())
                    .postTagVer2DTOS(hospital.getPostTags().stream()
                            .map(postTag -> new PostTagVer2DTO(postTag))
                            .collect(Collectors.toList()))
                    .reviewHospitalVer2DTOS(hospital.getReviewHospitals().stream()
                            .map(reviewHospital -> new ReviewHospitalVer2DTO(reviewHospital))
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
