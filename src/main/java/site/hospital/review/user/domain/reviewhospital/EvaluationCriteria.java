package site.hospital.review.user.domain.reviewhospital;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationCriteria {

    @NotNull
    private int sumPrice;
    @NotNull
    private int kindness;
    @NotNull
    private int symptomRelief;
    @NotNull
    private int cleanliness;
    @NotNull
    private int waitTime;
    @NotNull
    private double averageRate;

    @Builder
    public EvaluationCriteria(
            int sumPrice,
            int kindness,
            int symptomRelief,
            int cleanliness,
            int waitTime
    ) {
        this.sumPrice = sumPrice;
        this.kindness = kindness;
        this.symptomRelief = symptomRelief;
        this.cleanliness = cleanliness;
        this.waitTime = waitTime;
        this.averageRate = (double)(sumPrice + kindness + symptomRelief + cleanliness + waitTime) / 5;
    }
}
