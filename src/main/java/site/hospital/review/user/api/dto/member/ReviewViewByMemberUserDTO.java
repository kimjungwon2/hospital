package site.hospital.review.user.api.dto.member;

import lombok.Data;
import site.hospital.review.user.domain.reviewhospital.Recommendation;
import site.hospital.review.user.domain.reviewhospital.ReviewHospital;

@Data
public class ReviewViewByMemberUserDTO {
    private Long hospitalId;
    private String hospitalName;
    private String content;
    private String disease;
    private Recommendation recommendationStatus;
    private Integer sumPrice;
    private Integer kindness;
    private Integer symptomRelief;
    private Integer cleanliness;
    private Integer waitTime;
    private Double averageRate;

    public ReviewViewByMemberUserDTO(ReviewHospital reviewHospital) {
        this.hospitalId = reviewHospital.getHospital().getId();
        this.hospitalName = reviewHospital.getHospital().getHospitalName();
        this.content = reviewHospital.getContent();
        this.disease = reviewHospital.getDisease();
        this.recommendationStatus = reviewHospital.getRecommendationStatus();
        this.sumPrice = reviewHospital.getEvCriteria().getSumPrice();
        this.kindness = reviewHospital.getEvCriteria().getKindness();
        this.symptomRelief = reviewHospital.getEvCriteria().getSymptomRelief();
        this.cleanliness = reviewHospital.getEvCriteria().getCleanliness();
        this.waitTime = reviewHospital.getEvCriteria().getWaitTime();
        this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
    }
}
