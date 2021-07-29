package site.hospital.domain.appointment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.Hospital;
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    private LocalDateTime reservationDate;
    private String symptomName;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; //[APPOINTMENT,CANCEL,END] 상태

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

    public void cancel(){
        this.status = status.CANCEL;
    }

    public void finish(){
        this.status = status.END;
    }

    /*
    생성 메서드
    */
    @Builder
    public Appointment(LocalDateTime reservationDate, String symptomName){
        this.symptomName =symptomName;
        this.status = status.APPOINTMENT;
        this.reservationDate = reservationDate;
    }


    public static Appointment createAppointment(Member member, Hospital hospital, LocalDateTime reservationDate,
                                                String symptomName){
        Appointment appointment = new Appointment(reservationDate,symptomName);
        appointment.changeMember(member);
        appointment.changeHospital(hospital);

        return appointment;
    }

}
