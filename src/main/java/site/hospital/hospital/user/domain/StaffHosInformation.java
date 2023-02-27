package site.hospital.hospital.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseEntity;
import site.hospital.doctor.manager.domain.Doctor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffHosInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staffHosInformation_id")
    private Long id;

    @OneToOne(mappedBy = "staffHosInformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    @OneToMany(mappedBy = "staffHosInformation", cascade = CascadeType.ALL)
    private final List<Doctor> doctors = new ArrayList<>();

    @NotNull
    @Column(length = 1000)
    private String introduction;
    @NotNull
    @Column(length = 500)
    private String consultationHour;
    @NotNull
    @Column(length = 1000)
    private String abnormality;


    @Builder
    public StaffHosInformation(
            String introduction,
            String consultationHour,
            String abnormality
    ) {
        this.introduction = introduction;
        this.consultationHour = consultationHour;
        this.abnormality = abnormality;
    }

    public static StaffHosInformation createStaffHosInformation(
            StaffHosInformation staffHosInformation,
            List<Doctor> doctors
    ) {

        for (Doctor doctor : doctors) {
            staffHosInformation.addDoctor(doctor);
        }

        return staffHosInformation;
    }

    //연관관계 때문에 set 설정
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    //연관 관계 메서드
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        doctor.setStaffHosInformation(this);
    }

    public void modifyHospitalAdditionalInfo(StaffHosInformation hospitalAdditionalInfo) {
        this.introduction = hospitalAdditionalInfo.getIntroduction();
        this.consultationHour = hospitalAdditionalInfo.getConsultationHour();
        this.abnormality = hospitalAdditionalInfo.getAbnormality();
    }


}
