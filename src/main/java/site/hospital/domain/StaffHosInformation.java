package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffHosInformation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staffHosInformation_id")
    private long id;

    @OneToOne(mappedBy = "staffHosInformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    @OneToMany(mappedBy = "staffHosInformation",cascade = CascadeType.ALL)
    private List<Doctor> doctors = new ArrayList<>();

    private String photo;
    private String introduction;
    private String consultationHour;
    private String abnormality;

    //연관관계 때문에 set 설정
    public void setHospital(Hospital hospital){
        this.hospital = hospital;
    }



    //생성자

    @Builder
    public StaffHosInformation(String photo, String introduction, String consultationHour, String abnormality) {
        this.photo = photo;
        this.introduction = introduction;
        this.consultationHour = consultationHour;
        this.abnormality = abnormality;
    }

    public static StaffHosInformation createDoctor(Doctor doctor){
        StaffHosInformation staffHosInformation = new StaffHosInformation();
        staffHosInformation.addDoctor(doctor);

        return staffHosInformation;
    }

    public void modifyStaffHosInformation(String photo, String introduction,
                                          String consultationHour, String abnormality) {
        this.photo = photo;
        this.introduction = introduction;
        this.consultationHour = consultationHour;
        this.abnormality = abnormality;
    }

}
