package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.domain.detailedHosInformation.HospitalAddress;
import site.hospital.domain.detailedHosInformation.HospitalLocation;
import site.hospital.dto.CreateHospitalRequest;
import site.hospital.domain.Hospital;
import site.hospital.dto.ModifyHospitalRequest;
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

        //상세 정보를 기입할 경우 hospital+상세정보 저장.
        if(request.getHospitalAddress()!=null && request.getHospitalLocation()!=null)
        {
            DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                    .numberWard(request.getNumberWard()).numberPatientRoom(request.getNumberPatientRoom())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider())
                    .hospitalLocation(request.getHospitalLocation())
                    .hospitalAddress(request.getHospitalAddress()).build();

            Long id = hospitalService.register(hospital, detailedHosInformation);

            return new CreateHospitalResponse(id);
        }
        //상세정보를 기입하지 않을 경우 hospital 정보만 저장.
        else
        {
            Long id = hospitalService.registerHospital(hospital);
            return new CreateHospitalResponse(id);
        }

    }

    //병원 전체 검색
    @PostMapping("/search/hospital")
    public Page<HospitalSearchDto> searchHospital(@RequestBody @Validated HospitalSearchCondition condition, Pageable pageable){
        //" " 띄엄표 검색어 null로 인식.
        if(condition.getSearchName().equals(" ")) return null;

        return hospitalService.searchHospital(condition, pageable);
    }

    //병원 정보 등록(직원용)
    @PostMapping("/hospital/register/staff")
    public CreateStaffHospitalResponse saveStaffHospitalResponse(@RequestBody @Validated CreateStaffHospitalRequest request){
        Long id = hospitalService.registerStaffHosInformation(request.getHospitalId(),request.getPhoto(),
                request.getIntroduction(),request.getConsultationHour(),request.getAbnormality());

        return new CreateStaffHospitalResponse(id);
    }

    //병원 + 상세 내용 수정
    @PostMapping("/admin/hospital/modify")
    public CreateHospitalResponse saveHospitalResponse(@RequestBody @Validated ModifyHospitalRequest request){

        //상세 정보가 있을 경우 hospital+상세정보 저장.
        Long id = hospitalService.modifyAllHosInformation(request.getHospitalId(), request.getDetailedHosInformationId(),
                request);

        return new CreateHospitalResponse(id);

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
        public CreateStaffHospitalResponse(long id){ this.id = id; }
    }

    @Data
    private static class CreateStaffHospitalRequest{
        Long hospitalId;
        String photo;
        String introduction;
        String consultationHour;
        String abnormality;
        int numberHealthcareProvider;
        int numberWard;
        int numberPatientRoom;

        HospitalAddress hospitalAddress;
        HospitalLocation hospitalLocation;
    }

}
