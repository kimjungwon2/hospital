package site.hospital.hospital.manager.api;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
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
import site.hospital.hospital.manager.api.dto.view.ManagerHospitalViewResponse;
import site.hospital.hospital.manager.api.dto.ManagerModifyHospitalRequest;
import site.hospital.hospital.manager.service.ManagerHospitalService;
import site.hospital.hospital.user.api.dto.HospitalAdminViewThumbnailResponse;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateHosAdditionalInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.api.dto.HospitalViewImageResponse;
import site.hospital.hospital.user.domain.HospitalThumbnail;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerHospitalController {

    private final HospitalThumbnailImageService hospitalThumbnailImageService;
    private final HospitalImagesService hospitalImagesService;
    private final ManagerHospitalService managerHospitalService;


    @GetMapping("/staff/hospital/view")
    public ManagerHospitalViewResponse managerViewHospital(ServletRequest servletRequest) {
        return managerHospitalService.viewHospital(servletRequest);
    }

    @PutMapping("/staff/hospital/modify/{hospitalId}")
    public HospitalResponse managerModifyHospital(
            ServletRequest servletRequest,
            @PathVariable("hospitalId") Long hospitalId,
            @RequestBody @Validated ManagerModifyHospitalRequest request
    ) {
        return managerHospitalService.modifyHospital(servletRequest, hospitalId, request);
    }

    @PostMapping("/staff/hospital/register/detailed")
    public HospitalResponse managerRegisterDetailedHosInfo(
            ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateDetailHosInfoRequest request
    ) {
        return managerHospitalService.registerDetailedHosInfo(servletRequest, request);
    }

    @PostMapping("/staff/hospital/register/staffHosInfo")
    public HospitalResponse managerRegisterHosAdditionalInfo(
            ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateHosAdditionalInfoRequest request
    ) {
        return managerHospitalService.registerHosAdditionalInfo(servletRequest, request);
    }

    //수정 예정
    @DeleteMapping("/staff/{memberId}/detailedHos/delete/{detailedHosInfoId}")
    public void managerDeleteDetailedHospitalInfo(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("detailedHosInfoId") Long detailedHosInfoId
    ) {
        managerHospitalService.deleteDetailedHospitalInfo(servletRequest, detailedHosInfoId);
    }

    @PostMapping("/staff/hospital/register/thumbnail")
    public String managerRegisterThumbnail(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        return hospitalThumbnailImageService.uploadImage(imageFile, hospitalId);
    }

    @GetMapping("/staff/hospital/view/thumbnail")
    public HospitalAdminViewThumbnailResponse managerViewThumbnail(
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId) {
        HospitalThumbnail hospitalThumbnail = managerHospitalService.viewThumbnail(thumbnailId);

        return HospitalAdminViewThumbnailResponse.from(hospitalThumbnail);
    }

    @DeleteMapping("/staff/hospital/delete/thumbnail/{thumbnailId}")
    public void managerDeleteThumbnail(@PathVariable("thumbnailId") Long thumbnailId) {
        hospitalThumbnailImageService.deleteImage(thumbnailId);
    }

    @PostMapping("/staff/hospital/register/images")
    public List<String> managerRegisterHospitalImages(
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        List<String> ImageURLS = hospitalImagesService.uploadImage(imageFiles, hospitalId);

        return ImageURLS;
    }

    @GetMapping("/staff/hospital/view/hospitalImages")
    public List<HospitalViewImageResponse> managerViewHospitalImages(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId) {
        return managerHospitalService.viewHospitalImages(hospitalId);
    }

    @DeleteMapping("/staff/hospital/delete/hospitalImages/{hospitalImageId}")
    public void managerDeleteHospitalImage(@PathVariable("hospitalImageId") Long hospitalImageId) {
        hospitalImagesService.deleteImage(hospitalImageId);
    }

}
