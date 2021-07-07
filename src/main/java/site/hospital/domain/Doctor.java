package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="doctor_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detailed_hos_information_id")
    private DetailedHosInformation detailedHosInformation;

    private String name;
    private String history;
    private String photo;

    //연관관계 때문에 set 설정
    public void setDetailedHosInformation(DetailedHosInformation detailedHosInformation){
        this.detailedHosInformation = detailedHosInformation;
    }

    @Builder
    public Doctor(String name, String history, String photo) {
        this.name = name;
        this.history = history;
        this.photo = photo;
    }
}
