package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "appointment")
    private List<AppointmentHospital> appointmentHospitals = new ArrayList<>();

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member){
        this.member = member;
        member.getAppointments().add(this);
    }



    /*
    생성 메서드
    */

    public void addAppointmentHospital(AppointmentHospital appointmentHospital){
        appointmentHospitals.add(appointmentHospital);
    }

}
