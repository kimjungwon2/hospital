package site.hospital.hospital.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.hospital.admin.service.AdminHospitalAdditionalInfoService;
import site.hospital.hospital.user.api.dto.additionalinfo.HospitalAdditionalInfoViewResponse;
import site.hospital.hospital.admin.repository.dto.hospitalAdditionalInfo.AdminModifyStaffHosRequest;
import site.hospital.hospital.user.service.HospitalAdditionalInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminHospitalAdditionalInfoController {

    private final AdminHospitalAdditionalInfoService adminHospitalAdditionalInfoService;
    private final HospitalAdditionalInfoService hospitalAdditionalInfoService;

    //관리자 병원 추가 정보 보기
    @GetMapping("/admin/staffHosInfo/{staffHosId}")
    public HospitalAdditionalInfoViewResponse adminViewStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId) {
        return hospitalAdditionalInfoService.viewHospitalAdditionalInfo(staffHosId);
    }

    //관리자 추가 정보 수정하기
    @PutMapping("/admin/staffHosInfo/modify/{staffHosId}")
    public void modifyStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId,
            @RequestBody @Validated AdminModifyStaffHosRequest request
    ) {
        adminHospitalAdditionalInfoService.adminModifyStaffHosInfo(staffHosId, request);
    }

    //관리자 추가 정보 삭제하기
    @DeleteMapping("/admin/staffHosInfo/delete/{staffHosId}")
    public void deleteStaffHosInfo(@PathVariable("staffHosId") Long staffHosId) {
        adminHospitalAdditionalInfoService.adminDeleteStaffHosInfo(staffHosId);
    }

}
