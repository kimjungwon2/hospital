package site.hospital.review.user.api.dto.detail;

import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.review.user.domain.reviewhospital.EvaluationCriteria;
import site.hospital.review.user.domain.reviewhospital.Recommendation;
import site.hospital.review.user.domain.reviewhospital.ReviewHospital;

@Data
public class ReviewViewDetailHospitalDTO {
    private String content;
    private String disease;
    private String hospitalName;
    private String medicalSubjectInformation;
    Recommendation recommendationStatus;
    EvaluationCriteria evaluationCriteria;

    public ReviewViewDetailHospitalDTO(ReviewHospital reviewHospital) {
        Assert.notNull(reviewHospital.getHospital(),"hospital must be provided");

        this.hospitalName = reviewHospital.getHospital().getHospitalName();
        this.medicalSubjectInformation = reviewHospital.getHospital()
                .getMedicalSubjectInformation();
        this.content = reviewHospital.getContent();
        this.disease = reviewHospital.getDisease();
        this.recommendationStatus = reviewHospital.getRecommendationStatus();
        this.evaluationCriteria = reviewHospital.getEvCriteria();
    }
}
