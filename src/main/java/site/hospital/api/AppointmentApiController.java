package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.service.AppointmentService;

@RestController
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;

    @PostMapping("/hospital/appointment")
    public CreateAppointmentResponse saveAppointment(@RequestBody @Validated CreateAppointmentRequest request){
        System.out.println("request: "+request);
        Long id = appointmentService.appointment(request.getMemberId(),request.getHospitalId(),
                request.getYear(),request.getMonth(),request.getDay(),request.getHour(),request.getMinute());
        System.out.println("Id: "+id);
        return new CreateAppointmentResponse(id);
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

}
