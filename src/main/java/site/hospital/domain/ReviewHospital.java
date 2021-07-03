package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewHospital extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name="review_hospital_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private String disease;
    private int likeNumber;


    //평가 항목
    @Embedded
    private EvaluationCriteria evCriteria;

    private String content;
    //인증 상태
    private String authenticationStatus;
    //병원 추천 비추천 상태 [RECOMMENDATION, DECOMMENDATION]
    @Enumerated(EnumType.STRING)
    private Recommendation recommendationStatus;

}
