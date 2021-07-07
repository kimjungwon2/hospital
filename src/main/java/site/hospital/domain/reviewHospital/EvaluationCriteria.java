package site.hospital.domain.reviewHospital;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationCriteria {

    private int sumPrice;
    private int kindness;
    private int symptomRelief;
    private int cleanliness;
    private int waitTime;

    @Builder
    public EvaluationCriteria(int sumPrice, int kindness, int symptomRelief,
                              int cleanliness, int waitTime) {
        this.sumPrice = sumPrice;
        this.kindness = kindness;
        this.symptomRelief = symptomRelief;
        this.cleanliness = cleanliness;
        this.waitTime = waitTime;
    }
}
