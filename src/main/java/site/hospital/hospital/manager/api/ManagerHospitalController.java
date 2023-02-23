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
import site.hospital.hospital.manager.repository.dto.StaffHospitalView;
import site.hospital.hospital.manager.repository.dto.StaffModifyHospitalRequest;
import site.hospital.hospital.manager.service.ManagerHospitalService;
import site.hospital.hospital.user.api.dto.HospitalAdminViewThumbnailResponse;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateStaffHosInfoRequest;
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


    //병원 관계자 병원 보기
    @GetMapping("/staff/hospital/view")
    public StaffHospitalView viewHospital(ServletRequest servletRequest) {
        return managerHospitalService.staffViewHospital(servletRequest);
    }

    //병원 관계자 병원 수정
    @PutMapping("/staff/hospital/modify/{hospitalId}")
    public HospitalResponse staffUpdateHospital(
            ServletRequest servletRequest,
            @PathVariable("hospitalId") Long hospitalId,
            @RequestBody @Validated StaffModifyHospitalRequest request
    ) {
        return managerHospitalService.staffUpdateHospital(servletRequest, hospitalId, request);
    }

    //병원 관계자 상세 정보 등록
    @PostMapping("/staff/hospital/register/detailed")
    public HospitalResponse staffCreateDetailedHosInfo(
            ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateDetailHosInfoRequest request
    ) {
        return managerHospitalService
                .staffRegisterDetailHospitalInformation(servletRequest, request);
    }

    //병원 관계자 병원 추가 정보 등록
    @PostMapping("/staff/hospital/register/staffHosInfo")
    public HospitalResponse staffCreateStaffHosInfo(
            ServletRequest servletRequest,
            @RequestBody @Validated HospitalManagerCreateStaffHosInfoRequest request
    ) {
        return managerHospitalService.staffCreateStaffHosInfo(servletRequest, request);
    }

    //병원 관계자 상세 정보 삭제
    @DeleteMapping("/staff/{memberId}/detailedHos/delete/{detailedHosInfoId}")
    public void deleteDetailedHospitalInformation(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("detailedHosInfoId") Long detailedHosInfoId
    ) {
        managerHospitalService
                .staffDeleteDetailHospitalInformation(servletRequest, memberId, detailedHosInfoId);
    }

    //관계자 섬네일 등록
    @PostMapping("/staff/hospital/register/thumbnail")
    public String staffRegisterThumbnail(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        return hospitalThumbnailImageService.uploadImage(imageFile, "thumbnail", hospitalId);
    }

    //관계자 섬네일 보기
    @GetMapping("/staff/hospital/view/thumbnail")
    public HospitalAdminViewThumbnailResponse staffViewThumbnail(
            @RequestParam(value = "thumbnailId", required = false) Long thumbnailId) {
        HospitalThumbnail hospitalThumbnail = managerHospitalService.viewThumbnail(thumbnailId);

        return HospitalAdminViewThumbnailResponse.from(hospitalThumbnail);
    }

    //관계자 섬네일 삭제
    @DeleteMapping("/staff/hospital/delete/thumbnail/{thumbnailId}")
    public void staffDeleteThumbnail(@PathVariable("thumbnailId") Long thumbnailId) {
        hospitalThumbnailImageService.deleteImage(thumbnailId, "thumbnail");
    }

    //관계자 이미지 등록
    @PostMapping("/staff/hospital/register/images")
    public List<String> staffRegisterHospitalImages(
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "hospitalId", required = false) Long hospitalId
    ) throws IOException {
        List<String> ImageURLS = hospitalImagesService
                .uploadImages(imageFiles, "hospitalImage", hospitalId);

        return ImageURLS;
    }

    //관계자 이미지 보기
    @GetMapping("/staff/hospital/view/hospitalImages")
    public List<HospitalViewImageResponse> staffViewHospitalImages(
            @RequestParam(value = "hospitalId", required = false) Long hospitalId) {
        return managerHospitalService.viewHospitalImages(hospitalId);
    }

    //관계자 병원 이미지 삭제
    @DeleteMapping("/staff/hospital/delete/hospitalImages/{hospitalImageId}")
    public void staffDeleteHospitalImage(@PathVariable("hospitalImageId") Long hospitalImageId) {
        hospitalImagesService.deleteImage(hospitalImageId, "hospitalImage");
    }

}
