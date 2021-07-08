package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Appointment;
import site.hospital.service.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;

    @PostMapping("/hospital/appointment")
    public CreateAppointmentResponse saveAppointment(@RequestBody @Validated CreateAppointmentRequest request){
        Long id = appointmentService.appointment(request.getMemberId(),request.getHospitalId(),
                request.getYear(),request.getMonth(),request.getDay(),request.getHour(),request.getMinute());

        return new CreateAppointmentResponse(id);
    }

    //예약 목록 전체 조회(관리자)
    @GetMapping("/search/appointment")
    public List<SearchAppointmentResponse> searchAppointment(){
        List<Appointment> appointments = appointmentService.searchAdminAppointment();
        List<SearchAppointmentResponse> result = appointments.stream()
                .map(a->new SearchAppointmentResponse(a))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/member/appointment/{memberId}")
    public List<SearchAppointmentResponse> searchMemberAppointment(@PathVariable("memberId") Long memberId){
        List<Appointment> appointments = appointmentService.searchMemberAppointment(memberId);
        List<SearchAppointmentResponse> result = appointments.stream()
                .map(a->new SearchAppointmentResponse(a))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/hospital/appointment/{hospitalId}")
    public List<SearchAppointmentResponse> searchHospitalAppointment(@PathVariable("hospitalId") Long hospitalId){
        List<Appointment> appointments = appointmentService.searchHospitalAppointment(hospitalId);
        List<SearchAppointmentResponse> result = appointments.stream()
                .map(a->new SearchAppointmentResponse(a))
                .collect(Collectors.toList());

        return result;
    }


    /* DTO */
    @Data
    private static class CreateAppointmentResponse {
        long id;

        public CreateAppointmentResponse(long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateAppointmentRequest{
        private Long memberId;
        private Long hospitalId;
        private Integer year;
        private Integer month;
        private Integer day;
        private Integer hour;
        private Integer minute;
    }

    @Data
    private static class SearchAppointmentResponse{
        Long appointmentId;
        Long memberId;
        Long hospitalId;

        String userName;
        String hospitalName;
        LocalDateTime createTime;
        LocalDateTime reservationTime;

        public SearchAppointmentResponse(Appointment appointment) {
            this.appointmentId = appointment.getId();
            this.memberId = appointment.getMember().getId();
            this.hospitalId = appointment.getHospital().getId();
            this.userName = appointment.getMember().getUserName();
            this.hospitalName = appointment.getHospital().getHospitalName();
            this.createTime = appointment.getCreatedDate();
            this.reservationTime = appointment.getReservationDate();
        }
    }

}
