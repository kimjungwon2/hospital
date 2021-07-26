package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.dto.staffHosInfo.AdminDoctorDTO;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="doctor_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffHosInformation_id")
    private StaffHosInformation staffHosInformation;

    private String name;
    private String history;
    private String photo;

    //연관 관계 때문에 설정.
    public void setStaffHosInformation(StaffHosInformation staffHosInformation) {
        this.staffHosInformation = staffHosInformation;
    }


    @Builder
    public Doctor(StaffHosInformation staffHosInformation, String name, String history, String photo) {
        this.staffHosInformation = staffHosInformation;
        this.name = name;
        this.history = history;
        this.photo = photo;
    }

    public void modifyDoctor(String name, String history, String photo) {
        this.name = name;
        this.history = history;
        this.photo = photo;
    }

    //수정 메서드
    public void modifyDoctor(Doctor doctor){
        this.photo = doctor.getPhoto();
        this.name = doctor.getName();
        this.history = doctor.getHistory();
    }
}
