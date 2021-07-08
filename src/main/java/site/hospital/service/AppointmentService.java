package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.appointment.Appointment;
import site.hospital.domain.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.repository.Appointment.AppointmentRepository;
import site.hospital.repository.HospitalRepository;
import site.hospital.repository.member.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;


    //병원 예약
    @Transactional
    public Long appointment(Long memberId, Long hospitalId,
                            Integer year, Integer month, Integer day, Integer hour, Integer minute, String symptomName){

        Member member = memberRepository.findById(memberId).orElse(null);
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        Appointment appointment = Appointment.createAppointment(member,hospital,LocalDateTime.of(year,month,day,hour,minute),symptomName);
        appointmentRepository.save(appointment);

        return appointment.getId();
    }
    //관리자 전체 예약 조회
    public List<Appointment> searchAdminAppointment(){
        List<Appointment> appointments = appointmentRepository.searchAppointment(null,null);

        return appointments;
    }

    //멤버 예약 조회
    public List<Appointment> searchMemberAppointment(Long memberId){
        List<Appointment> appointments = appointmentRepository.searchAppointment(memberId,null);

        return appointments;
    }

    //병원 예약 조회
    public List<Appointment> searchHospitalAppointment(Long hospitalId){
        List<Appointment> appointments = appointmentRepository.searchAppointment(null, hospitalId);

        return appointments;
    }

    //예약 취소
    public void cancelAppointment(Long appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

        appointment.cancel();
    }

    //진료 중료.
    public void finishAppointment(Long appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

        appointment.finish();
    }

}
