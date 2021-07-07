package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.dto.CreateHospitalRequest;
import site.hospital.domain.Hospital;
import site.hospital.service.HospitalService;

@RestController
@RequiredArgsConstructor
public class HospitalApiController {

    private final HospitalService hospitalService;

    //병원 생성
    @PostMapping("/hospital/register")
    public CreateHospitalResponse saveHospital(@RequestBody @Validated CreateHospitalRequest request){
        Hospital hospital = Hospital.builder()
                .licensingDate(request.getLicensingDate())
                .hospitalName(request.getHospitalName())
                .phoneNumber(request.getPhoneNumber())
                .distinguishedName(request.getDistinguishedName())
                .medicalSubject(request.getMedicalSubject())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .businessCondition(request.getBusinessCondition())
                .cityName(request.getCityName())
                .build();
        Long id = hospitalService.register(hospital);

        return new CreateHospitalResponse(id);
    }

    @PostMapping("/hospital/register/staff")
    public Long CreateStaffHospitalResponse(@RequestBody @Validated CreateStaffHospitalRequest request){
        Long id = hospitalService.registerStaffHosInformation(request.getPhoto(),
                request.getIntroduction(),request.getConsultationHour(),request.getAbnormality());

        return id;
    }

    /* DTO */
    @Data
    private static class CreateHospitalResponse {
        long id;
        public CreateHospitalResponse(long id){
            this.id = id;
        }
    }

    @Data
    private static class CreateStaffHospitalResponse {
        long id;
        public CreateStaffHospitalResponse(long id){
            this.id = id;
        }
    }

    @Data
    private static class CreateStaffHospitalRequest{
        String photo;
        String introduction;
        String consultationHour;
        String abnormality;
    }

}
