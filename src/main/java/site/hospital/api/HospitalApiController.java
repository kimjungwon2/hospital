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

import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.domain.hospital.Hospital;
import site.hospital.dto.AdminHospitalSearchCondition;
import site.hospital.dto.hospital.admin.AdminModifyHospitalRequest;
import site.hospital.dto.doctor.CreateDoctorRequest;
import site.hospital.dto.hospital.admin.AdminHospitalView;
import site.hospital.dto.hospital.staff.StaffHospitalView;
import site.hospital.dto.hospital.staff.StaffModifyHospitalRequest;
import site.hospital.repository.hospital.adminSearchQuery.AdminSearchHospitalDto;
import site.hospital.repository.hospital.searchQuery.HospitalSearchDto;
import site.hospital.repository.hospital.viewQuery.ViewHospitalDTO;
import site.hospital.service.HospitalService;

import javax.servlet.ServletRequest;
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


    //병원 관계자 병원 보기
    @GetMapping("/staff/hospital/view")
    public StaffHospitalView viewHospital(ServletRequest servletRequest){
        Hospital hospital = hospitalService.staffViewHospital(servletRequest);

        //NullPointerException 방지.
        Long detailedHosId;
        Long staffHosId;

        if(hospital.getDetailedHosInformation() == null) detailedHosId = null;
        else detailedHosId = hospital.getDetailedHosInformation().getId();

        if(hospital.getStaffHosInformation() == null) staffHosId = null;
        else staffHosId = hospital.getStaffHosInformation().getId();

        StaffHospitalView staffHospitalView = new StaffHospitalView(hospital, detailedHosId, staffHosId);

        return staffHospitalView;
    }

    //병원 관계자 병원 수정
    @PutMapping("/staff/hospital/modify/{hospitalId}")
    public StaffUpdateHospitalResponse staffUpdateHospital(ServletRequest servletRequest, @PathVariable("hospitalId") Long hospitalId,
                                                      @RequestBody @Validated StaffModifyHospitalRequest request){
        Long findId = hospitalService.staffUpdateHospital(servletRequest, request.getMemberId(),hospitalId, request);

        return new StaffUpdateHospitalResponse(findId);
    }

    //병원 관계자 상세 정보 등록
    @PostMapping("/staff/hospital/register/detailed")
    public CreateDetailedHosResponse staffCreateDetailedHosInfo(ServletRequest servletRequest,
                                                                     @RequestBody @Validated StaffCreateDetailedHospitalInfoRequest request){
        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                .numberPatientRoom(request.getNumberPatientRoom()).numberWard(request.getNumberWard())
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .hospitalLocation(request.getHospitalLocation()).hospitalAddress(request.getHospitalAddress()).build();

        Long detailedHosId = hospitalService.staffRegisterDetailHospitalInformation(servletRequest, request.getMemberId(),
                detailedHosInformation, request.getHospitalId());

        return new CreateDetailedHosResponse(detailedHosId);
    }

    //병원 관계자 병원 추가 정보 등록
    @PostMapping("/staff/hospital/register/staffHosInfo")
    public CreateStaffHosResponse staffCreateStaffHosInfo(ServletRequest servletRequest, @RequestBody @Validated StaffCreateStaffHosRequest request){

        StaffHosInformation staffHosInformation = StaffHosInformation.builder().abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour()).introduction(request.getIntroduction()).build();

        //의사 정보가 있어야지 의사 추가.
        if(request.getDoctors()!=null) {
            List<Doctor> doctors = request.getDoctors().stream().map(d -> new Doctor(d)).collect(Collectors.toList());
            Long id = hospitalService.staffRegisterStaffHosInformation(servletRequest, request.getMemberId(),request.getHospitalId(), staffHosInformation, doctors);
            return new CreateStaffHosResponse(id);
        }
        //추가 정보만 추가.
        else {
            Long id = hospitalService.staffRegisterStaffHosInfo(servletRequest, request.getMemberId(), request.getHospitalId(), staffHosInformation);
            return new CreateStaffHosResponse(id);
        }
    }

    //병원 관계자 상세 정보 삭제
    @DeleteMapping("/staff/{memberId}/detailedHos/delete/{detailedHosInfoId}")
    public void deleteDetailedHospitalInformation(ServletRequest servletRequest, @PathVariable("memberId") Long memberId, @PathVariable("detailedHosInfoId") Long detailedHosInfoId){
        hospitalService.staffDeleteDetailHospitalInformation(servletRequest, memberId, detailedHosInfoId);
    }

    //관리자 병원 검색
    @GetMapping("/admin/hospital/search")
    public Page<AdminSearchHospitalDto> adminSearchHospitals(@RequestParam(value="hospitalId",required = false) Long hospitalId,
                                                             @RequestParam(value="hospitalName",required = false) String hospitalName,
                                                             @RequestParam(value="businessCondition",required = false) BusinessCondition businessCondition,
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
    @DeleteMapping("/admin/detailedHos/delete/{detailedHosInfoId}")
    public void deleteDetailedHospitalInformation(@PathVariable("detailedHosInfoId") Long detailedHosInfoId){
        hospitalService.deleteDetailHospitalInformation(detailedHosInfoId);
    }

    //관리자 상세 정보 등록
    @PostMapping("/admin/hospital/register/detailed")
    public CreateDetailedHosResponse adminCreateDetailedHosInfo(@RequestBody @Validated CreateDetailedHospitalInformationRequest request){
        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                .numberPatientRoom(request.getNumberPatientRoom()).numberWard(request.getNumberWard())
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .hospitalLocation(request.getHospitalLocation()).hospitalAddress(request.getHospitalAddress()).build();

        Long detailedHosId = hospitalService.registerDetailHospitalInformation(detailedHosInformation, request.getHospitalId());

        return new CreateDetailedHosResponse(detailedHosId);
    }

    //관리자 병원 추가 정보 등록
    @PostMapping("/admin/hospital/register/staff")
    public CreateStaffHosResponse adminCreateStaffHosInfo(@RequestBody @Validated CreateStaffHosRequest request){

        StaffHosInformation staffHosInformation = StaffHosInformation.builder().abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour()).introduction(request.getIntroduction()).build();

        //의사 정보가 있어야지 의사 추가.
        if(request.getDoctors()!=null) {
            List<Doctor> doctors = request.getDoctors().stream().map(d -> new Doctor(d)).collect(Collectors.toList());
            Long id = hospitalService.adminRegisterStaffHosInformation(request.getHospitalId(),staffHosInformation, doctors);
            return new CreateStaffHosResponse(id);
        }
        //추가 정보만 추가.
        Long id = hospitalService.adminRegisterStaffHosInfo(request.getHospitalId(), staffHosInformation);
        return new CreateStaffHosResponse(id);
    }


    /* DTO */
    @Data
    private static class AdminUpdateHospitalResponse{
        Long id;

        public AdminUpdateHospitalResponse(Long id) {
            this.id = id;
        }
    }

    /* DTO */
    @Data
    private static class StaffUpdateHospitalResponse{
        Long id;

        public StaffUpdateHospitalResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateStaffHosResponse{
        Long id;
        public CreateStaffHosResponse(Long id) {
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
    private static class CreateDetailedHosResponse {
        long id;
        public CreateDetailedHosResponse(long id){ this.id = id; }
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
        private BusinessCondition businessCondition;
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
    private static class StaffCreateDetailedHospitalInfoRequest{
        private Long hospitalId;
        private Long memberId;
        private Integer numberHealthcareProvider;
        private Integer numberWard;
        private Integer numberPatientRoom;

        private HospitalLocation hospitalLocation;
        private HospitalAddress hospitalAddress;
    }

    @Data
    private static class CreateStaffHosRequest{
        private Long hospitalId;
        private String introduction;
        private String consultationHour;
        private String abnormality;
        private List<CreateDoctorRequest> doctors;
    }

    @Data
    private static class StaffCreateStaffHosRequest{
        private Long hospitalId;
        private Long memberId;
        private String introduction;
        private String consultationHour;
        private String abnormality;
        private List<CreateDoctorRequest> doctors;
    }

}
