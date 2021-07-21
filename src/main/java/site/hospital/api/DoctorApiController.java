package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.Doctor;
import site.hospital.service.DoctorService;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class DoctorApiController {

    private final DoctorService doctorService;

    //의사 등록
    @PostMapping("/staff/doctor/register")
    public CreateDoctorsResponse registerDoctor(@RequestBody @Validated CreateDoctorsRequest request){
        Doctor doctor = Doctor.builder()
                .name(request.getName()).history(request.getHistory()).photo(request.getPhoto()).build();

        Long id = doctorService.registerDoctor(request.getStaffHosId(),doctor);

        return new CreateDoctorsResponse(id);
    }

    /*DTO*/
    @Data
    private static class CreateDoctorsResponse{
        Long doctorId;

        public CreateDoctorsResponse(Long doctorId) {
            this.doctorId = doctorId;
        }
    }

    @Data
    private static class CreateDoctorsRequest{
        @NotNull(message="상세 병원의 FK를 기입해주세요.")
        private Long staffHosId;
        private String name;
        private String history;
        private String photo;
    }
}
