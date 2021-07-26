package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Doctor;
import site.hospital.dto.doctor.CreateDoctorRequest;
import site.hospital.service.DoctorService;

@RestController
@RequiredArgsConstructor
public class DoctorApiController {

    private final DoctorService doctorService;

    @PostMapping("/admin/doctor/register")
    public CreateDoctorResponse saveDoctor(@RequestBody @Validated CreateDoctorRequest request){
        Long id = doctorService.createDoctor(request);
        return new CreateDoctorResponse(id);
    }

    @PutMapping("/admin/doctor/modify/{doctorId}")
    public void modifyDoctor(@PathVariable("doctorId") Long doctorId,
                             @RequestBody @Validated AdminModifyDoctorRequest request){
        Doctor doctor = Doctor.builder()
                .photo(request.getPhoto()).history(request.getHistory())
                .name(request.getName()).build();

        doctorService.modifyDoctor(doctorId, doctor);
    }

    @DeleteMapping("/admin/doctor/delete/{doctorId}")
    public void deleteDoctor(@PathVariable("doctorId") Long doctorId){
        doctorService.deleteDoctor(doctorId);
    }


    @Data
    private static class CreateDoctorResponse {
        Long DoctorId;
        public CreateDoctorResponse(Long DoctorId){
            this.DoctorId = DoctorId;
        }
    }

    @Data
    private static class AdminModifyDoctorRequest{
        private String photo;
        private String history;
        private String name;
    }
}
