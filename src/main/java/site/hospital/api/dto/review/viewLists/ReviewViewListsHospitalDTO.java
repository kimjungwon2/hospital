package site.hospital.api.dto.review.viewLists;

import lombok.Data;
import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.domain.reviewHospital.ReviewHospital;

@Data
public class ReviewViewListsHospitalDTO {
    private String content;
    private String disease;
    private Integer sumPrice;
    private Integer kindness;
    private Integer symptomRelief;
    private Integer cleanliness;
    private Integer waitTime;
    private Double averageRate;
    private Recommendation recommendationStatus;

    public ReviewViewListsHospitalDTO(ReviewHospital reviewHospital) {
        this.content = reviewHospital.getContent();
        this.disease = reviewHospital.getDisease();
        this.sumPrice = reviewHospital.getEvCriteria().getSumPrice();
        this.kindness = reviewHospital.getEvCriteria().getKindness();
        this.symptomRelief = reviewHospital.getEvCriteria().getSymptomRelief();
        this.cleanliness = reviewHospital.getEvCriteria().getCleanliness();
        this.waitTime = reviewHospital.getEvCriteria().getWaitTime();
        this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
        this.recommendationStatus = reviewHospital.getRecommendationStatus();
    }

}
