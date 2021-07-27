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
    private Long id;

    @OneToOne(mappedBy = "staffHosInformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    @OneToMany(mappedBy = "staffHosInformation", cascade = CascadeType.ALL)
    private List<Doctor> doctors = new ArrayList<>();
    @OneToMany(mappedBy = "staffHosInformation", cascade = CascadeType.ALL)
    private List<HospitalImage> hospitalImages = new ArrayList<>();


    private String introduction;
    private String consultationHour;
    private String abnormality;

    //연관관계 때문에 set 설정
    public void setHospital(Hospital hospital){
        this.hospital = hospital;
    }

    //연관 관계 메서드
    public void addDoctor(Doctor doctor){
        doctors.add(doctor);
        doctor.setStaffHosInformation(this);
    }

    public void addHospitalImages(HospitalImage hospitalImage){
        hospitalImages.add(hospitalImage);
        hospitalImage.setStaffHosInformation(this);
    }

    //사진 생성 메서드
    public static StaffHosInformation createHospitalImage(StaffHosInformation staffHosInformation,List<HospitalImage> hospitalImages){
        for(HospitalImage hospitalImage : hospitalImages){
            staffHosInformation.addHospitalImages(hospitalImage);
        }
        return staffHosInformation;
    }


    //생성 메서드
    public static StaffHosInformation createStaffHosInformation(StaffHosInformation staffHosInformation, List<Doctor> doctors){

        for(Doctor doctor : doctors){
            staffHosInformation.addDoctor(doctor);
        }

        return staffHosInformation;
    }

    //수정 메서드
    public void modifyStaffHosInformation(StaffHosInformation staffHosInformation){
        this.introduction = staffHosInformation.getIntroduction();
        this.consultationHour = staffHosInformation.getConsultationHour();
        this.abnormality = staffHosInformation.getAbnormality();
    }

    //생성자
    @Builder
    public StaffHosInformation(String introduction, String consultationHour, String abnormality) {
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
