package site.hospital.dto.hospital.admin;

import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.domain.reviewHospital.ReviewHospital;

public class AdminReviewHospitalDTO {
    private Long reviewId;
    private Long reviewHospitalId;

    private String content;
    private String disease;
    private Recommendation recommedationStatus;

    private Integer sumPrice;
    private Integer kindness;
    private Integer symptomRelief;
    private Integer cleanliness;
    private Integer waitTime;
    private Double averageRate;

    public AdminReviewHospitalDTO(ReviewHospital reviewHospital) {
        this.reviewId = reviewHospital.getReview().getId();
        this.reviewHospitalId = reviewHospital.getId();
        this.content = reviewHospital.getContent();
        this.disease = reviewHospital.getDisease();
        this.recommedationStatus = reviewHospital.getRecommendationStatus();
        this.sumPrice = reviewHospital.getEvCriteria().getSumPrice();
        this.kindness = reviewHospital.getEvCriteria().getKindness();
        this.symptomRelief = reviewHospital.getEvCriteria().getSymptomRelief();
        this.cleanliness = reviewHospital.getEvCriteria().getCleanliness();
        this.waitTime = reviewHospital.getEvCriteria().getWaitTime();
        this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
    }
}
