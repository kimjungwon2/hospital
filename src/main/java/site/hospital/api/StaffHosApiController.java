package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Doctor;
import site.hospital.domain.StaffHosInformation;
import site.hospital.service.StaffHosService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StaffHosApiController {

    private final StaffHosService staffHosService;


    //병원 추가 정보 보기(고객)
    @GetMapping("/hospital/staffHosInfo/{staffHosId}")
    public StaffHosInfoView viewStaffHosInfo(@PathVariable("staffHosId") Long staffHosId){
        StaffHosInformation staffHosInformation = staffHosService.viewStaffHosInfo(staffHosId);
        StaffHosInfoView staffHosInfoView = new StaffHosInfoView(staffHosInformation);

        return staffHosInfoView;
    }

    /*DTO*/
    @Data
    private static class StaffHosInfoView{
        private String photo;
        private String introduction;
        private String consultationHour;
        private String abnormality;
        private List<doctorDTO> doctors;

        public StaffHosInfoView(StaffHosInformation staffHosInformation) {
            this.photo = staffHosInformation.getPhoto();
            this.introduction = staffHosInformation.getIntroduction();
            this.consultationHour = staffHosInformation.getConsultationHour();
            this.abnormality = staffHosInformation.getAbnormality();
            this.doctors = staffHosInformation.getDoctors().stream()
                    .map(d->new doctorDTO(d))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class doctorDTO{
        private String name;
        private String history;
        private String photo;

        public doctorDTO(Doctor doctor) {
            this.name = doctor.getName();
            this.history = doctor.getHistory();
            this.photo = doctor.getPhoto();
        }
    }

}
