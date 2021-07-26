package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private String cityName;
    private String hospitalName;
    private String distinctionGrade;
    private String estimationList;

    //== 연관 관계 메서드 ==/
    public void changeHospital(Hospital hospital){
        this.hospital = hospital;
        hospital.getEstimations().add(this);
    }

    public void modifyEstimation(Estimation estimation){
        this.city = estimation.getCity();
        this.hospitalName = estimation.getHospitalName();
        this.distinctionGrade = estimation.getDistinctionGrade();
    }

    @Builder
    public Estimation(String cityName, String hospitalName,
                      String distinctionGrade, String estimationList) {
        this.cityName = cityName;
        this.hospitalName = hospitalName;
        this.distinctionGrade = distinctionGrade;
        this.estimationList = estimationList;
    }
}
