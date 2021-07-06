package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Appointment;
import site.hospital.domain.Hospital;
import site.hospital.domain.Member;
import site.hospital.repository.AppointmentRepository;
import site.hospital.repository.HospitalRepository;
import site.hospital.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;


    //병원 예약
    @Transactional
    public Long appointment(long memberId, long hospitalId,
                            int year, int month, int day, int hour, int minute){

        Member member = memberRepository.findById(memberId).orElse(null);
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        Appointment appointment = Appointment.createAppointment(member,hospital,LocalDateTime.of(year,month,day,hour,minute));
        appointmentRepository.save(appointment);

        return appointment.getId();
    }

}
