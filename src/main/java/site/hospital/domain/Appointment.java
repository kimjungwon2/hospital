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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member){
        this.member = member;
        member.getAppointments().add(this);
    }

    public void changeHospital(Hospital hospital){
        this.hospital = hospital;
        hospital.getAppointments().add(this);
    }


    /*
    생성 메서드
    */

    public Appointment createAppointment(Member member, Hospital hospital){
        Appointment appointment = new Appointment();
        appointment.changeMember(member);
        appointment.changeHospital(hospital);

        return appointment;
    }

}
