package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;

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

    //== 연관 관계 메서드 ==//
    public void changeStaffHosInformation(StaffHosInformation staffHosInformation){
        this.staffHosInformation = staffHosInformation;
        staffHosInformation.getDoctors().add(this);
    }

    @Builder
    public Doctor(String name, String history, String photo) {
        this.name = name;
        this.history = history;
        this.photo = photo;
    }

    public void modifyDoctor(String name, String history, String photo) {
        this.name = name;
        this.history = history;
        this.photo = photo;
    }


}
