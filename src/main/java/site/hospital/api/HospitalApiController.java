package site.hospital.api;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.api.dto.hospital.HospitalAdminViewThumbnailResponse;
import site.hospital.api.dto.hospital.HospitalCreateDetailedHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalCreateRequest;
import site.hospital.api.dto.hospital.HospitalCreateStaffHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalManagerCreateStaffHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalResponse;
import site.hospital.api.dto.hospital.HospitalViewImageResponse;
import site.hospital.domain.Doctor;
import site.hospital.domain.HospitalImage;
import site.hospital.domain.HospitalThumbnail;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.domain.hospital.Hospital;
import site.hospital.dto.AdminHospitalSearchCondition;
import site.hospital.dto.hospital.admin.AdminHospitalView;
import site.hospital.dto.hospital.admin.AdminModifyHospitalRequest;
import site.hospital.dto.hospital.staff.StaffHospitalView;
import site.hospital.dto.hospital.staff.StaffModifyHospitalRequest;
import site.hospital.repository.hospital.adminSearchQuery.AdminSearchHospitalDto;
import site.hospital.repository.hospital.searchQuery.HospitalSearchDto;
import site.hospital.repository.hospital.viewQuery.ViewHospitalDTO;
import site.hospital.service.HospitalService;
import site.hospital.service.ImageManagementService;

@RestController
@RequiredArgsConstructor
public class HospitalApiController {

    private final HospitalService hospitalService;
    private final ImageManagementService imageManagementService;


    //병원 전체 검색
    @GetMapping("/search/hospital/{searchName}")
    public Page<HospitalSearchDto> searchHospital(@PathVariable("searchName") String searchName,
            Pageable pageable) {

        return hospitalService.searchHospital(searchName, pageable);
    }

    //병원 정보 보기(고객)
    @GetMapping("/hospital/view/{hospitalId}")
    public ViewHospitalDTO viewsHospital(@PathVariable("hospitalId") Long hospitalId) {
        return hospitalService.viewHospital(hospitalId);
    }


    //병원 관계자 병원 보기
    @GetMapping("/staff/hospital/view")
    public StaffHospitalView viewHospital(ServletRequest servletRequest) {
        Hospital hospital = hospitalService.staffViewHospital(servletRequest);

        //NullPointerException 방지.
        Long detailedHosId;
        Long staffHosId;
        Long hospitalThumbnailId;

        if (hospital.getDetailedHosInformation() == null) {
            detailedHosId = null;
        } else {
            detailedHosId = hospital.getDetailedHosInformation().getId();
        }

        if (hospital.getStaffHosInformation() == null) {
            staffHosId = null;
        } else {
            staffHosId = hospital.getStaffHosInformation().getId();
        }

        if (hospital.getHospitalThumbnail() == null) {
            hospitalThumbnailId = null;
        } else {
            hospitalThumbnailId = hospital.getHospitalThumbnail().getId();
        }

        StaffHospitalView staffHospitalView = new StaffHospitalView(hospital, detailedHosId,
                staffHosId, hospitalThumbnailId);

        return staffHospitalView;
    }

    //병원 관계자 병원 수정
    @PutMapping("/staff/hospital/modify/{hospitalId}")
    public HospitalResponse staffUpdateHospital(ServletRequest servletRequest,
            @PathVariable("hospitalId") Long hospitalId,
            @RequestBody @Validated StaffModifyHospitalRequest request) {
        Long findId = hospitalService
                .staffUpdateHospital(servletRequest, request.getMemberId(), hospitalId, request);

        return HospitalResponse.from(findId);
    }

    //병원 관계자 상세 정보 등록
    @PostMapping("/staff/hospital/register/detailed")
    public HospitalResponse staffCreateDetailedHosInfo(ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateDetailHosInfoRequest request) {
        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                .numberPatientRoom(request.getNumberPatientRoom())
                .numberWard(request.getNumberWard())
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .hospitalLocation(request.getHospitalLocation())
                .hospitalAddress(request.getHospitalAddress()).build();

        Long detailedHosId = hospitalService
                .staffRegisterDetailHospitalInformation(servletRequest, request.getMemberId(),
                        detailedHosInformation, request.getHospitalId());

        return HospitalResponse.from(detailedHosId);
    }

    //병원 관계자 병원 추가 정보 등록
    @PostMapping("/staff/hospital/register/staffHosInfo")
    public HospitalResponse staffCreateStaffHosInfo(ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateStaffHosInfoRequest request) {

        StaffHosInformation staffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        //의사 정보가 있어야지 의사 추가.
        if (request.getDoctors() != null) {
            List<Doctor> doctors = request.getDoctors().stream().map(d -> new Doctor(d))
                    .collect(Collectors.toList());
            Long id = hospitalService
                    .staffRegisterStaffHosInformation(servletRequest, request.getMemberId(),
                            request.getHospitalId(), staffHosInformation, doctors);
            return HospitalResponse.from(id);
        }
        //추가 정보만 추가.
        else {
            Long id = hospitalService
                    .staffRegisterStaffHosInfo(servletRequest, request.getMemberId(),
                            request.getHospitalId(), staffHosInformation);
            return HospitalResponse.from(id);
        }
    }

    //병원 관계자 상세 정보 삭제
    @DeleteMapping("/staff/{memberId}/detailedHos/delete/{detailedHosInfoId}")
    public void deleteDetailedHospitalInformation(ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("detailedHosInfoId") Long detailedHosInfoId) {
        hospitalService
                .staffDeleteDetailHospitalInformation(servletRequest, memberId, detailedHosInfoId);
    }

    //관계자 섬네일 등록
    @PostMapping("/staff/hospital/register/thumbnail")
    public String staffRegisterThumbnail(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId)
            throws IOException {
        String ImageURL = imageManagementService
                .thumbnailUpload(imageFile, "thumbnail", hospitalId);

        return ImageURL;
    }

    //관계자 섬네일 보기
    @GetMapping("/staff/hospital/view/thumbnail")
    public HospitalAdminViewThumbnailResponse staffViewThumbnail(
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId) {
        HospitalThumbnail hospitalThumbnail = hospitalService.viewThumbnail(thumbnailId);

        return HospitalAdminViewThumbnailResponse.from(hospitalThumbnail);
    }

    //관계자 섬네일 삭제
    @DeleteMapping("/staff/hospital/delete/thumbnail/{thumbnailId}")
    public void staffDeleteThumbnail(@PathVariable("thumbnailId") Long thumbnailId) {
        imageManagementService.deleteThumbnail(thumbnailId, "thumbnail");
    }

    //관계자 이미지 등록
    @PostMapping("/staff/hospital/register/images")
    public List<String> staffRegisterHospitalImages(
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId)
            throws IOException {
        List<String> ImageURLS = imageManagementService
                .hospitalImageUpload(imageFiles, "hospitalImage", hospitalId);

        return ImageURLS;
    }

    //관계자 이미지 보기
    @GetMapping("/staff/hospital/view/hospitalImages")
    public List<HospitalViewImageResponse> staffViewHospitalImages(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId) {
        List<HospitalImage> hospitalImages = hospitalService.viewHospitalImages(hospitalId);
        List<HospitalViewImageResponse> images = hospitalImages.stream()
                .map(h -> HospitalViewImageResponse.from(h))
                .collect(Collectors.toList());

        return images;
    }

    //관계자 병원 이미지 삭제
    @DeleteMapping("/staff/hospital/delete/hospitalImages/{hospitalImageId}")
    public void staffDeleteHospitalImage(@PathVariable("hospitalImageId") Long hospitalImageId) {
        imageManagementService.deleteHospitalImage(hospitalImageId, "hospitalImage");
    }


    //관리자 병원 검색
    @GetMapping("/admin/hospital/search")
    public Page<AdminSearchHospitalDto> adminSearchHospitals(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "businessCondition", required = false) BusinessCondition businessCondition,
            @RequestParam(value = "cityName", required = false) String cityName,
            Pageable pageable) {
        AdminHospitalSearchCondition condition = AdminHospitalSearchCondition.builder()
                .hospitalId(hospitalId).hospitalName(hospitalName)
                .businessCondition(businessCondition).cityName(cityName).build();

        return hospitalService.adminSearchHospitals(condition, pageable);
    }

    //관리자 병원 생성
    @PostMapping("/admin/hospital/register")
    public HospitalResponse saveHospital(
            @RequestBody @Validated HospitalCreateRequest request) {
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
        if (request.getDetailedInfoCheck() == null) {
            throw new IllegalStateException("상세 정보 체크를 하세요.");
        }

        //상세정보를 체크하지 않을 경우 hospital 정보만 저장.
        if (request.getDetailedInfoCheck() == false) {
            Long id = hospitalService.registerHospital(hospital);
            return HospitalResponse.from(id);
        }
        //상세 정보를 체크할 경우 hospital + 상세정보 저장.
        else if (request.getDetailedInfoCheck() == true &&
                request.getHospitalLocation() != null
                && request.getHospitalLocation().getLatitude() != null
                && request.getHospitalLocation().getLongitude() != null
                && request.getHospitalLocation().getX_coordination() != null
                && request.getHospitalLocation().getX_coordination() != null
                && request.getHospitalLocation().getY_coordination() != null
                && request.getHospitalAddress() != null
                && request.getHospitalAddress().getLandLotBasedSystem() != null
                && request.getHospitalAddress().getRoadBaseAddress() != null
                && request.getHospitalAddress().getZipCode() != null
                && request.getNumberHealthcareProvider() != null && request.getNumberWard() != null
                && request.getNumberPatientRoom() != null) {
            DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                    .numberWard(request.getNumberWard())
                    .numberPatientRoom(request.getNumberPatientRoom())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider())
                    .hospitalLocation(request.getHospitalLocation())
                    .hospitalAddress(request.getHospitalAddress()).build();

            Long id = hospitalService.register(hospital, detailedHosInformation);

            return HospitalResponse.from(id);
        }
        //상세 정보를 체크했는데도 상세 정보를 모두 기입 안 할 경우
        else {
            throw new IllegalStateException("상세 정보를 모두 기입하세요");
        }
    }


    //관리자 병원 보기
    @GetMapping("/admin/hospital/view")
    public AdminHospitalView viewHospital(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId,
            @RequestParam(value = "detailedHosInfoId", required = false) Long detailedHosInfoId,
            @RequestParam(value = "staffHosInfoId", required = false) Long staffHosInfoId,
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId) {
        Hospital hospital = hospitalService.adminViewHospital(hospitalId);
        AdminHospitalView adminHospitalView = new AdminHospitalView(hospital, detailedHosInfoId,
                staffHosInfoId, thumbnailId);

        return adminHospitalView;
    }

    //관리자 병원 수정
    @PutMapping("/admin/hospital/modify/{hospitalId}")
    public HospitalResponse updateHospital(@PathVariable("hospitalId") Long hospitalId,
            @RequestBody @Validated AdminModifyHospitalRequest request) {
        Long findId = hospitalService.adminUpdateHospital(hospitalId, request);

        return HospitalResponse.from(findId);
    }

    //관리자 병원 삭제
    @DeleteMapping("/admin/hospital/delete/{hospitalId}")
    public void deleteHospital(@PathVariable("hospitalId") Long hospitalId,
            @RequestParam(value = "staffHosInfoId", required = false) Long staffHosInfoId) {
        hospitalService.adminDeleteHospital(hospitalId, staffHosInfoId);
    }

    //관리자 병원 상세 정보 삭제
    @DeleteMapping("/admin/detailedHos/delete/{detailedHosInfoId}")
    public void deleteDetailedHospitalInformation(
            @PathVariable("detailedHosInfoId") Long detailedHosInfoId) {
        hospitalService.deleteDetailHospitalInformation(detailedHosInfoId);
    }


    //관리자 병원 추가 정보 등록
    @PostMapping("/admin/hospital/register/staff")
    public HospitalResponse adminCreateStaffHosInfo(
            @RequestBody @Validated HospitalCreateStaffHosInfoRequest request) {

        StaffHosInformation staffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        //의사 정보가 있어야지 의사 추가.
        if (request.getDoctors() != null) {
            List<Doctor> doctors = request.getDoctors().stream().map(d -> new Doctor(d))
                    .collect(Collectors.toList());
            Long id = hospitalService
                    .adminRegisterStaffHosInformation(request.getHospitalId(), staffHosInformation,
                            doctors);
            return HospitalResponse.from(id);
        }
        //추가 정보만 추가.
        Long id = hospitalService
                .adminRegisterStaffHosInfo(request.getHospitalId(), staffHosInformation);
        return HospitalResponse.from(id);
    }

    //관리자 상세 정보 등록
    @PostMapping("/admin/hospital/register/detailed")
    public HospitalResponse adminCreateDetailedHosInfo(
            @RequestBody @Validated HospitalCreateDetailedHosInfoRequest request) {
        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                .numberPatientRoom(request.getNumberPatientRoom())
                .numberWard(request.getNumberWard())
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .hospitalLocation(request.getHospitalLocation())
                .hospitalAddress(request.getHospitalAddress()).build();

        Long detailedHosId = hospitalService
                .registerDetailHospitalInformation(detailedHosInformation, request.getHospitalId());

        return HospitalResponse.from(detailedHosId);
    }

    //관리자 섬네일 등록
    @PostMapping("/admin/hospital/register/thumbnail")
    public String adminRegisterThumbnail(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId)
            throws IOException {
        String ImageURL = imageManagementService
                .thumbnailUpload(imageFile, "thumbnail", hospitalId);

        return ImageURL;
    }


    //관리자 섬네일 보기
    @GetMapping("/admin/hospital/view/thumbnail")
    public HospitalAdminViewThumbnailResponse viewThumbnail(
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId) {
        HospitalThumbnail hospitalThumbnail = hospitalService.viewThumbnail(thumbnailId);

        return HospitalAdminViewThumbnailResponse.from(hospitalThumbnail);
    }

    //관리자 섬네일 삭제
    @DeleteMapping("/admin/hospital/delete/thumbnail/{thumbnailId}")
    public void adminDeleteThumbnail(@PathVariable("thumbnailId") Long thumbnailId) {
        imageManagementService.deleteThumbnail(thumbnailId, "thumbnail");
    }

    //관리자 이미지 등록
    @PostMapping("/admin/hospital/register/images")
    public List<String> adminRegisterHospitalImages(
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId)
            throws IOException {
        List<String> ImageURLS = imageManagementService
                .hospitalImageUpload(imageFiles, "hospitalImage", hospitalId);

        return ImageURLS;
    }

    //관리자 이미지 보기
    @GetMapping("/admin/hospital/view/hospitalImages")
    public List<HospitalViewImageResponse> adminViewHospitalImages(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId) {
        List<HospitalImage> hospitalImages = hospitalService.viewHospitalImages(hospitalId);
        List<HospitalViewImageResponse> images = hospitalImages.stream()
                .map(h -> HospitalViewImageResponse.from(h))
                .collect(Collectors.toList());

        return images;
    }

    //관리자 병원 이미지 삭제
    @DeleteMapping("/admin/hospital/delete/hospitalImages/{hospitalImageId}")
    public void adminDeleteHospitalImage(@PathVariable("hospitalImageId") Long hospitalImageId) {
        imageManagementService.deleteHospitalImage(hospitalImageId, "hospitalImage");
    }

}
