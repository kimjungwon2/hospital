package site.hospital.review.user.api.dto.viewDetail;

import lombok.Data;
import site.hospital.review.user.domain.reviewHospital.EvaluationCriteria;
import site.hospital.review.user.domain.reviewHospital.Recommendation;
import site.hospital.review.user.domain.reviewHospital.ReviewHospital;

@Data
public class ReviewViewDetailHospitalDTO {
    private String content;
    private String disease;
    private String hospitalName;
    private String medicalSubjectInformation;
    Recommendation recommendationStatus;
    EvaluationCriteria evaluationCriteria;

    public ReviewViewDetailHospitalDTO(ReviewHospital reviewHospital) {
        this.hospitalName = reviewHospital.getHospital().getHospitalName();
        this.medicalSubjectInformation = reviewHospital.getHospital()
                .getMedicalSubjectInformation();
        this.content = reviewHospital.getContent();
        this.disease = reviewHospital.getDisease();
        this.recommendationStatus = reviewHospital.getRecommendationStatus();
        this.evaluationCriteria = reviewHospital.getEvCriteria();
    }
}
