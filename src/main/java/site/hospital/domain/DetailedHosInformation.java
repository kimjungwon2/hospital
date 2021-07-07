package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailedHosInformation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailed_hos_information_id")
    private long id;

    @OneToOne(mappedBy = "detailedHosInformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    @OneToMany(mappedBy = "detailedHosInformation",cascade = CascadeType.ALL)
    private List<Doctor> doctors = new ArrayList<>();

    private String photo;
    private String introduction;
    private String consultationHour;
    private String abnormality;

    //연관관계 때문에 set 설정
    public void setHospital(Hospital hospital){
        this.hospital = hospital;
    }

    /*연관관계 메서드*/
    public void addDoctor(Doctor doctor){
        doctors.add(doctor);
        doctor.setDetailedHosInformation(this);
    }

    //생성자

    @Builder
    public DetailedHosInformation(String photo, String introduction, String consultationHour, String abnormality) {
        this.photo = photo;
        this.introduction = introduction;
        this.consultationHour = consultationHour;
        this.abnormality = abnormality;
    }

    public static DetailedHosInformation createDoctor(Doctor doctor){
        DetailedHosInformation detailedHosInformation = new DetailedHosInformation();
        detailedHosInformation.addDoctor(doctor);

        return detailedHosInformation;
    }
}
