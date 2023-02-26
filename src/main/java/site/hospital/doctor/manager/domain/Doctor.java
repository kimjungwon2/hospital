package site.hospital.doctor.manager.domain;

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
import site.hospital.common.domain.BaseEntity;
import site.hospital.doctor.admin.api.dto.DoctorAdminCreateRequest;
import site.hospital.hospital.user.domain.StaffHosInformation;

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


    public Doctor(DoctorAdminCreateRequest request) {
        this.name = request.getName();
        this.history = request.getHistory();
    }

    //연관 관계 때문에 설정.
    public void setStaffHosInformation(StaffHosInformation staffHosInformation) {
        this.staffHosInformation = staffHosInformation;
    }

    public void modifyDoctor(Doctor doctor) {
        this.name = doctor.getName();
        this.history = doctor.getHistory();
    }
}
