package site.hospital.domain.estimation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.domain.hospital.Hospital;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @NotNull
    private String cityName;
    @NotNull
    private String hospitalName;

    @NotNull
    private String distinctionGrade;
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstimationList estimationList;

    //== 연관 관계 메서드 ==/
    public void changeHospital(Hospital hospital) {
        this.hospital = hospital;
        hospital.getEstimations().add(this);
    }

    //평가 수정하기
    public void modifyEstimation(Estimation estimation) {
        this.estimationList = estimation.getEstimationList();
        this.distinctionGrade = estimation.getDistinctionGrade();
    }

    @Builder
    public Estimation(String cityName, String hospitalName,
            String distinctionGrade, EstimationList estimationList) {
        this.cityName = cityName;
        this.hospitalName = hospitalName;
        this.distinctionGrade = distinctionGrade;
        this.estimationList = estimationList;
    }
}
