package site.hospital.api;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.*;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.domain.detailedHosInformation.HospitalAddress;
import site.hospital.domain.detailedHosInformation.HospitalLocation;

import site.hospital.domain.Hospital;
import site.hospital.dto.AdminHospitalSearchCondition;
import site.hospital.dto.AdminModifyHospitalRequest;
import site.hospital.dto.doctor.CreateDoctorRequest;
import site.hospital.dto.hospital.admin.AdminHospitalView;
import site.hospital.repository.hospital.adminSearchQuery.AdminSearchHospitalDto;
import site.hospital.repository.hospital.searchQuery.HospitalSearchDto;
import site.hospital.repository.hospital.viewQuery.ViewHospitalDTO;
import site.hospital.service.HospitalService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HospitalApiController {

    private final HospitalService hospitalService;


    //병원 전체 검색
    @GetMapping("/search/hospital/{searchName}")
    public Page<HospitalSearchDto> searchHospital(@PathVariable("searchName") String searchName, Pageable pageable){

        return hospitalService.searchHospital(searchName, pageable);
    }


    //병원 정보 보기(고객)
    @GetMapping("/hospital/view/{hospitalId}")
    public ViewHospitalDTO viewsHospital(@PathVariable("hospitalId") Long hospitalId){
        return hospitalService.viewHospital(hospitalId);
    }

    //관리자 병원 검색
    @GetMapping("/admin/hospital/search")
    public Page<AdminSearchHospitalDto> adminSearchHospitals(@RequestParam(value="hospitalId",required = false) Long hospitalId,
                                                             @RequestParam(value="hospitalName",required = false) String hospitalName,
                                                             @RequestParam(value="businessCondition",required = false) String businessCondition,
                                                             @RequestParam(value="cityName",required = false) String cityName,
                                                             Pageable pageable){
        AdminHospitalSearchCondition condition = AdminHospitalSearchCondition.builder()
                .hospitalId(hospitalId).hospitalName(hospitalName).businessCondition(businessCondition).cityName(cityName).build();

        return hospitalService.adminSearchHospitals(condition, pageable);
    }

    //관리자 병원 생성
    @PostMapping("/admin/hospital/register")
    public CreateHospitalResponse saveHospital(@RequestBody @Validated CreateHospitalRequest request) {
        Hospital hospital = Hospital.builder()
                .licensingDate(request.getLicensingDate())
                .hospitalName(request.getHospitalName())
                .phoneNumber(request.getPhoneNumber())
                .distinguishedName(request.getDistinguishedName())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .businessCondition(request.getBusinessCondition())
                .cityName(request.getCityName())
                .build();

        //상세정보 체크를 기입하지 않을 경우
        if(request.getDetailedInfoCheck() == null){
            throw new IllegalStateException("상세 정보 체크를 하세요.");
        }

        //상세정보를 체크하지 않을 경우 hospital 정보만 저장.
        if (request.getDetailedInfoCheck() == false) {
            Long id = hospitalService.registerHospital(hospital);
            return new CreateHospitalResponse(id);
        }
        //상세 정보를 체크할 경우 hospital + 상세정보 저장.
        else if (request.getDetailedInfoCheck() == true &&
                request.getHospitalLocation() != null && request.getHospitalLocation().getLatitude() != null
                && request.getHospitalLocation().getLongitude() !=null && request.getHospitalLocation().getX_coordination() !=null
                && request.getHospitalLocation().getX_coordination()!=null && request.getHospitalLocation().getY_coordination()!=null
                && request.getHospitalAddress() != null && request.getHospitalAddress().getLandLotBasedSystem() != null
                && request.getHospitalAddress().getRoadBaseAddress() != null && request.getHospitalAddress().getZipCode()!=null
                && request.getNumberHealthcareProvider() != null && request.getNumberWard() != null
                && request.getNumberPatientRoom() != null )
        {
            DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                    .numberWard(request.getNumberWard()).numberPatientRoom(request.getNumberPatientRoom())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider())
                    .hospitalLocation(request.getHospitalLocation())
                    .hospitalAddress(request.getHospitalAddress()).build();

            Long id = hospitalService.register(hospital, detailedHosInformation);

            return new CreateHospitalResponse(id);
        }
        //상세 정보를 체크했는데도 상세 정보를 모두 기입 안 할 경우
        else throw new IllegalStateException("상세 정보를 모두 기입하세요");
    }


    //관리자 병원 보기
    @GetMapping("/admin/hospital/view")
    public AdminHospitalView viewHospital(@RequestParam(value="hospitalId",required = false) Long hospitalId,
                                          @RequestParam(value="detailedHosInfoId",required = false) Long detailedHosInfoId,
                                          @RequestParam(value="staffHosInfoId",required = false) Long staffHosInfoId){
        Hospital hospital = hospitalService.adminViewHospital(hospitalId);
        AdminHospitalView adminHospitalView = new AdminHospitalView(hospital, detailedHosInfoId, staffHosInfoId);

        return adminHospitalView;
    }

    //관리자 병원 수정
    @PutMapping("/admin/hospital/modify/{hospitalId}")
    public AdminUpdateHospitalResponse updateHospital(@PathVariable("hospitalId") Long hospitalId,
                                                      @RequestBody @Validated AdminModifyHospitalRequest request){
        Long findId = hospitalService.adminUpdateHospital(hospitalId, request);

        return new AdminUpdateHospitalResponse(findId);
    }

    //관리자 병원 삭제
    @DeleteMapping("/admin/hospital/delete/{hospitalId}")
    public void deleteHospital(@PathVariable("hospitalId") Long hospitalId,
                               @RequestParam(value="staffHosInfoId",required = false) Long staffHosInfoId){
        hospitalService.adminDeleteHospital(hospitalId, staffHosInfoId);
    }

    //관리자 병원 상세 정보 삭제
    @DeleteMapping("/admin/detailedHos/delete/{hospitalId}")
    public void deleteDetailedHospitalInformation(@PathVariable("hospitalId") Long detailedHosInfoId){
        hospitalService.deleteDetailHospitalInformation(detailedHosInfoId);
    }

    //관리자 상세 정보 등록
    @PostMapping("/admin/hospital/register/detailed")
    public AdminCreateDetailedHosReponse adminCreateDetailedHosInfo(@RequestBody @Validated CreateDetailedHospitalInformationRequest request){
        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                .numberPatientRoom(request.getNumberPatientRoom()).numberWard(request.getNumberWard())
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .hospitalLocation(request.getHospitalLocation()).hospitalAddress(request.getHospitalAddress()).build();

        Long detailedHosId = hospitalService.registerDetailHospitalInformation(detailedHosInformation, request.getHospitalId());

        return new AdminCreateDetailedHosReponse(detailedHosId);
    }

    //관리자 병원 추가 정보 등록
    @PostMapping("/admin/hospital/register/staff")
    public AdminCreateStaffHosResponse adminCreateStaffHosInfo(@RequestBody @Validated AdminCreateStaffHosRequest request){

        StaffHosInformation staffHosInformation = StaffHosInformation.builder().abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour()).introduction(request.getIntroduction()).build();

        //의사 정보가 있어야지 의사 추가.
        if(request.getDoctors()!=null) {
            List<Doctor> doctors = request.getDoctors().stream().map(d -> new Doctor(d)).collect(Collectors.toList());
            Long id = hospitalService.adminRegisterStaffHosInformation(request.getHospitalId(),staffHosInformation, doctors);
            return new AdminCreateStaffHosResponse(id);
        }
        //추가 정보만 추가.
        Long id = hospitalService.adminRegisterStaffHosInfo(request.getHospitalId(), staffHosInformation);
        return new AdminCreateStaffHosResponse(id);
    }


    /* DTO */
    @Data
    private static class AdminUpdateHospitalResponse{
        Long id;

        public AdminUpdateHospitalResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    private static class AdminCreateStaffHosResponse{
        Long id;

        public AdminCreateStaffHosResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateHospitalResponse {
        Long id;
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
    private static class AdminCreateDetailedHosReponse {
        long id;
        public AdminCreateDetailedHosReponse(long id){ this.id = id; }
    }

    @Data
    private static class CreateStaffHospitalRequest{
        Long hospitalId;
        String introduction;
        String consultationHour;
        String abnormality;
    }

    @Data
    private static class CreateHospitalRequest{

        private String licensingDate;
        private String hospitalName;
        private String phoneNumber;
        private String distinguishedName;
        private String medicalSubjectInformation;
        private String businessCondition;
        private String cityName;

        private Boolean detailedInfoCheck;

        private Integer numberHealthcareProvider;
        private Integer numberWard;
        private Integer numberPatientRoom;

        private HospitalLocation hospitalLocation;
        private HospitalAddress hospitalAddress;

    }

    @Data
    private static class CreateDetailedHospitalInformationRequest{
        private Long hospitalId;
        private Integer numberHealthcareProvider;
        private Integer numberWard;
        private Integer numberPatientRoom;

        private HospitalLocation hospitalLocation;
        private HospitalAddress hospitalAddress;
    }

    @Data
    private static class AdminCreateStaffHosRequest{
        private Long hospitalId;
        private String introduction;
        private String consultationHour;
        private String abnormality;
        private List<CreateDoctorRequest> doctors;
    }

}
