package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.CreateHospitalRequest;
import site.hospital.api.dto.CreateHospitalResponse;
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
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .numberWard(request.getNumberWard())
                .numberPatientRoom(request.getNumberPatientRoom())
                .hospitalAddress(request.getHospitalAddress())
                .hospitalLocation(request.getHospitalLocation())
                .build();
        Long id = hospitalService.register(hospital);

        return new CreateHospitalResponse(id);
    }

}
