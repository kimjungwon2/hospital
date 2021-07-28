package site.hospital.domain.reviewHospital;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.Hospital;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.review.Review;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewHospital extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_hospital_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private String content;
    private String disease;

    //평가 항목
    @Embedded
    private EvaluationCriteria evCriteria;

    //병원 추천 비추천 상태 [RECOMMENDATION, DECOMMENDATION]
    @Enumerated(EnumType.STRING)
    private Recommendation recommendationStatus;

    /*연관관계 때문에 set 설정*/
    public void setReview(Review review){
        this.review = review;
    }

    //== 생성 메서드 ==//

    @Builder
    public ReviewHospital(Hospital hospital, String content, String disease,EvaluationCriteria evCriteria,
                          Recommendation recommendationStatus) {
        this.hospital = hospital;
        this.content = content;
        this.disease = disease;
        this.evCriteria = evCriteria;
        this.recommendationStatus = recommendationStatus;
    }

    //리뷰 병원 작성
    public static ReviewHospital saveReviewHospital(Hospital hospital, ReviewHospital reviewHospitalDTO){
        //평가 기준 적용
        EvaluationCriteria evaluationCriteria = EvaluationCriteria.builder()
                .sumPrice(reviewHospitalDTO.getEvCriteria().getSumPrice())
                .kindness(reviewHospitalDTO.getEvCriteria().getKindness())
                .symptomRelief(reviewHospitalDTO.getEvCriteria().getSymptomRelief())
                .cleanliness(reviewHospitalDTO.getEvCriteria().getCleanliness())
                .waitTime(reviewHospitalDTO.getEvCriteria().getWaitTime()).build();

        //리뷰 병원 등록
        ReviewHospital reviewHospital = ReviewHospital.builder()
                .hospital(hospital)
                .content(reviewHospitalDTO.getContent()).disease(reviewHospitalDTO.getDisease())
                .evCriteria(evaluationCriteria)
                .recommendationStatus(reviewHospitalDTO.getRecommendationStatus()).build();

        return reviewHospital;
    }

}
