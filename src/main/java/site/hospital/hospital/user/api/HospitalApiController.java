package site.hospital.hospital.user.api;


import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.hospital.user.api.dto.HospitalAdminViewThumbnailResponse;
import site.hospital.hospital.user.api.dto.HospitalCreateDetailedHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateStaffHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateStaffHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.api.dto.HospitalViewImageResponse;
import site.hospital.hospital.user.domain.HospitalThumbnail;
import site.hospital.hospital.user.domain.BusinessCondition;
import site.hospital.hospital.admin.repository.dto.AdminHospitalView;
import site.hospital.hospital.admin.repository.dto.AdminModifyHospitalRequest;
import site.hospital.hospital.manager.repository.dto.StaffHospitalView;
import site.hospital.hospital.manager.repository.dto.StaffModifyHospitalRequest;
import site.hospital.hospital.user.repository.adminSearchQuery.AdminSearchHospitalDto;
import site.hospital.hospital.user.repository.searchQuery.HospitalSearchDto;
import site.hospital.hospital.user.repository.viewQuery.ViewHospitalDTO;
import site.hospital.hospital.user.service.HospitalService;
import site.hospital.common.service.ImageManagementService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HospitalApiController {

    private final HospitalService hospitalService;
    private final ImageManagementService imageManagementService;


    //병원 전체 검색
    @GetMapping("/search/hospital/{searchName}")
    public Page<HospitalSearchDto> searchHospital(
            @PathVariable("searchName") String searchName,
            Pageable pageable
    ) {
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
        return hospitalService.staffViewHospital(servletRequest);
    }

    //병원 관계자 병원 수정
    @PutMapping("/staff/hospital/modify/{hospitalId}")
    public HospitalResponse staffUpdateHospital(
            ServletRequest servletRequest,
            @PathVariable("hospitalId") Long hospitalId,
            @RequestBody @Validated StaffModifyHospitalRequest request
    ) {
        return hospitalService.staffUpdateHospital(servletRequest, hospitalId, request);
    }

    //병원 관계자 상세 정보 등록
    @PostMapping("/staff/hospital/register/detailed")
    public HospitalResponse staffCreateDetailedHosInfo(
            ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateDetailHosInfoRequest request
    ) {
        return hospitalService
                .staffRegisterDetailHospitalInformation(servletRequest, request);
    }

    //병원 관계자 병원 추가 정보 등록
    @PostMapping("/staff/hospital/register/staffHosInfo")
    public HospitalResponse staffCreateStaffHosInfo(
            ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateStaffHosInfoRequest request
    ) {
        return hospitalService.staffCreateStaffHosInfo(servletRequest, request);
    }

    //병원 관계자 상세 정보 삭제
    @DeleteMapping("/staff/{memberId}/detailedHos/delete/{detailedHosInfoId}")
    public void deleteDetailedHospitalInformation(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("detailedHosInfoId") Long detailedHosInfoId
    ) {
        hospitalService
                .staffDeleteDetailHospitalInformation(servletRequest, memberId, detailedHosInfoId);
    }

    //관계자 섬네일 등록
    @PostMapping("/staff/hospital/register/thumbnail")
    public String staffRegisterThumbnail(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        return imageManagementService
                .thumbnailUpload(imageFile, "thumbnail", hospitalId);
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
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        List<String> ImageURLS = imageManagementService
                .hospitalImageUpload(imageFiles, "hospitalImage", hospitalId);

        return ImageURLS;
    }

    //관계자 이미지 보기
    @GetMapping("/staff/hospital/view/hospitalImages")
    public List<HospitalViewImageResponse> staffViewHospitalImages(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId) {
        return hospitalService.viewHospitalImages(hospitalId);
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
            Pageable pageable
    ) {
        return hospitalService.adminSearchHospitals(
                hospitalId,
                hospitalName,
                businessCondition,
                cityName,
                pageable);
    }

    //관리자 병원 생성
    @PostMapping("/admin/hospital/register")
    public HospitalResponse saveHospital(
            @RequestBody @Validated HospitalCreateRequest request) {
        return hospitalService.adminSaveHospital(request);
    }

    //관리자 병원 보기
    @GetMapping("/admin/hospital/view")
    public AdminHospitalView viewHospital(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId,
            @RequestParam(value = "detailedHosInfoId", required = false) Long detailedHosInfoId,
            @RequestParam(value = "staffHosInfoId", required = false) Long staffHosInfoId,
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId
    ) {
        return hospitalService
                .adminViewHospital(hospitalId, detailedHosInfoId, staffHosInfoId, thumbnailId);
    }

    //관리자 병원 수정
    @PutMapping("/admin/hospital/modify/{hospitalId}")
    public HospitalResponse updateHospital(
            @PathVariable("hospitalId") Long hospitalId,
            @RequestBody @Validated AdminModifyHospitalRequest request
    ) {
        return hospitalService.adminUpdateHospital(hospitalId, request);
    }

    //관리자 병원 삭제
    @DeleteMapping("/admin/hospital/delete/{hospitalId}")
    public void deleteHospital(
            @PathVariable("hospitalId") Long hospitalId,
            @RequestParam(value = "staffHosInfoId", required = false) Long staffHosInfoId
    ) {
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
        return hospitalService.adminCreateStaffHosInfo(request);
    }

    //관리자 상세 정보 등록
    @PostMapping("/admin/hospital/register/detailed")
    public HospitalResponse adminCreateDetailedHosInfo(
            @RequestBody @Validated HospitalCreateDetailedHosInfoRequest request) {
        return hospitalService.registerDetailHospitalInformation(request, request.getHospitalId());
    }

    //관리자 섬네일 등록
    @PostMapping("/admin/hospital/register/thumbnail")
    public String adminRegisterThumbnail(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
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
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        List<String> ImageURLS = imageManagementService
                .hospitalImageUpload(imageFiles, "hospitalImage", hospitalId);

        return ImageURLS;
    }

    //관리자 이미지 보기
    @GetMapping("/admin/hospital/view/hospitalImages")
    public List<HospitalViewImageResponse> adminViewHospitalImages(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId) {
        return hospitalService.viewHospitalImages(hospitalId);
    }

    //관리자 병원 이미지 삭제
    @DeleteMapping("/admin/hospital/delete/hospitalImages/{hospitalImageId}")
    public void adminDeleteHospitalImage(@PathVariable("hospitalImageId") Long hospitalImageId) {
        imageManagementService.deleteHospitalImage(hospitalImageId, "hospitalImage");
    }

}
