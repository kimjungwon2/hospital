package site.hospital.estimation.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseEntity;
import site.hospital.hospital.user.domain.Hospital;

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
