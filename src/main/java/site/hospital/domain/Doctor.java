package site.hospital.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.dto.doctor.CreateDoctorRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffHosInformation_id")
    private StaffHosInformation staffHosInformation;

    @NotNull
    private String name;
    @NotNull
    private String history;


    @Builder
    public Doctor(StaffHosInformation staffHosInformation, String name, String history) {
        this.staffHosInformation = staffHosInformation;
        this.name = name;
        this.history = history;
    }


    public Doctor(CreateDoctorRequest request) {
        this.name = request.getName();
        this.history = request.getHistory();
    }

    //연관 관계 때문에 설정.
    public void setStaffHosInformation(StaffHosInformation staffHosInformation) {
        this.staffHosInformation = staffHosInformation;
    }

    //수정 메서드
    public void modifyDoctor(Doctor doctor) {
        this.name = doctor.getName();
        this.history = doctor.getHistory();
    }
}
