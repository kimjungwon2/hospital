package site.hospital.api.dto.review.member;

import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.domain.reviewHospital.ReviewHospital;

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
