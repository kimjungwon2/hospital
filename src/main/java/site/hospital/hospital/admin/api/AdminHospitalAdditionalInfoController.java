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

    @GetMapping("/admin/staffHosInfo/{staffHosId}")
    public HospitalAdditionalInfoViewResponse adminViewHospitalAdditionalInfo(
            @PathVariable("staffHosId") Long hosAdditionalInfoId) {
        return hospitalAdditionalInfoService.viewHospitalAdditionalInfo(hosAdditionalInfoId);
    }

    @PutMapping("/admin/staffHosInfo/modify/{staffHosId}")
    public void adminModifyHospitalAdditionalInfo(
            @PathVariable("staffHosId") Long hosAdditionalInfoId,
            @RequestBody @Validated AdminModifyStaffHosRequest request
    ) {
        adminHospitalAdditionalInfoService.modifyHospitalAdditionalInfo(hosAdditionalInfoId, request);
    }

    @DeleteMapping("/admin/staffHosInfo/delete/{staffHosId}")
    public void adminDeleteHospitalAdditionalInfo(@PathVariable("staffHosId") Long hosAdditionalInfoId) {
        adminHospitalAdditionalInfoService.deleteHospitalAdditionalInfo(hosAdditionalInfoId);
    }

}
