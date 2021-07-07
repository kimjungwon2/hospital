package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String city;
    private String hospitalName;
    private String distinctionGrade;
    private String estimationList;

    @Builder
    public Estimation(String city, String hospitalName, String distinctionGrade, String estimationList) {
        this.city = city;
        this.hospitalName = hospitalName;
        this.distinctionGrade = distinctionGrade;
        this.estimationList = estimationList;
    }
}
