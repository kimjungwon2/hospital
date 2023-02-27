package site.hospital.hospital.admin.api;

import java.io.IOException;
import java.util.List;
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
import site.hospital.common.service.image.HospitalImagesService;
import site.hospital.common.service.image.HospitalThumbnailImageService;
import site.hospital.hospital.admin.repository.search.AdminHospitalSearchSelectQuery;
import site.hospital.hospital.admin.repository.dto.AdminHospitalView;
import site.hospital.hospital.admin.repository.dto.AdminModifyHospitalRequest;
import site.hospital.hospital.admin.service.AdminHospitalService;
import site.hospital.hospital.manager.service.ManagerHospitalService;
import site.hospital.hospital.user.api.dto.HospitalAdminViewThumbnailResponse;
import site.hospital.hospital.user.api.dto.HospitalCreateDetailedHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateStaffHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.api.dto.HospitalViewImageResponse;
import site.hospital.hospital.user.domain.BusinessCondition;
import site.hospital.hospital.user.domain.HospitalThumbnail;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminHospitalController {

    private final AdminHospitalService adminHospitalService;
    private final ManagerHospitalService managerHospitalService;
    private final HospitalThumbnailImageService hospitalThumbnailImageService;
    private final HospitalImagesService hospitalImagesService;

    @GetMapping("/admin/hospital/search")
    public Page<AdminHospitalSearchSelectQuery> adminSearchHospitals(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "businessCondition", required = false) BusinessCondition businessCondition,
            @RequestParam(value = "cityName", required = false) String cityName,
            Pageable pageable
    ) {
        return adminHospitalService.searchHospitals(
                hospitalId,
                hospitalName,
                businessCondition,
                cityName,
                pageable);
    }

    @PostMapping("/admin/hospital/register")
    public HospitalResponse adminRegisterHospital(
            @RequestBody @Validated HospitalCreateRequest request) {
        return adminHospitalService.registerHospital(request);
    }

    @GetMapping("/admin/hospital/view")
    public AdminHospitalView adminViewHospital(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId,
            @RequestParam(value = "detailedHosInfoId", required = false) Long detailedHosInfoId,
            @RequestParam(value = "staffHosInfoId", required = false) Long staffHosInfoId,
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId
    ) {
        return adminHospitalService
                .viewHospital(hospitalId, detailedHosInfoId, staffHosInfoId, thumbnailId);
    }

    @PutMapping("/admin/hospital/modify/{hospitalId}")
    public HospitalResponse adminModifyHospital(
            @PathVariable("hospitalId") Long hospitalId,
            @RequestBody @Validated AdminModifyHospitalRequest request
    ) {
        return adminHospitalService.modifyHospital(hospitalId, request);
    }

    @DeleteMapping("/admin/hospital/delete/{hospitalId}")
    public void adminDeleteHospital(
            @PathVariable("hospitalId") Long hospitalId,
            @RequestParam(value = "staffHosInfoId", required = false) Long staffHosInfoId
    ) {
        adminHospitalService.deleteHospital(hospitalId, staffHosInfoId);
    }


    @DeleteMapping("/admin/detailedHos/delete/{detailedHosInfoId}")
    public void adminDeleteDetailedHospitalInfo(
            @PathVariable("detailedHosInfoId") Long detailedHosInfoId) {
        adminHospitalService.deleteDetailedHospitalInfo(detailedHosInfoId);
    }

    @PostMapping("/admin/hospital/register/staff")
    public HospitalResponse adminRegisterHosAdditionalInfo(
            @RequestBody @Validated HospitalCreateStaffHosInfoRequest request) {
        return adminHospitalService.registerHosAdditionalInfo(request);
    }

    @PostMapping("/admin/hospital/register/detailed")
    public HospitalResponse adminRegisterDetailedHosInfo(
            @RequestBody @Validated HospitalCreateDetailedHosInfoRequest request) {
        return adminHospitalService.registerDetailedHosInfo(request, request.getHospitalId());
    }

    @PostMapping("/admin/hospital/register/thumbnail")
    public String adminRegisterThumbnail(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        String ImageURL = hospitalThumbnailImageService.uploadImage(imageFile, hospitalId);

        return ImageURL;
    }

    @GetMapping("/admin/hospital/view/thumbnail")
    public HospitalAdminViewThumbnailResponse adminViewThumbnail(
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId) {
        HospitalThumbnail hospitalThumbnail = managerHospitalService.viewThumbnail(thumbnailId);

        return HospitalAdminViewThumbnailResponse.from(hospitalThumbnail);
    }

    @DeleteMapping("/admin/hospital/delete/thumbnail/{thumbnailId}")
    public void adminDeleteThumbnail(@PathVariable("thumbnailId") Long thumbnailId) {
        hospitalThumbnailImageService.deleteImage(thumbnailId);
    }

    @PostMapping("/admin/hospital/register/images")
    public List<String> adminRegisterHospitalImages(
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        List<String> ImageURLS = hospitalImagesService
                .uploadImage(imageFiles, hospitalId);

        return ImageURLS;
    }

    @GetMapping("/admin/hospital/view/hospitalImages")
    public List<HospitalViewImageResponse> adminViewHospitalImages(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId) {
        return managerHospitalService.viewHospitalImages(hospitalId);
    }

    @DeleteMapping("/admin/hospital/delete/hospitalImages/{hospitalImageId}")
    public void adminDeleteHospitalImage(@PathVariable("hospitalImageId") Long hospitalImageId) {
        hospitalImagesService.deleteImage(hospitalImageId);
    }

}
