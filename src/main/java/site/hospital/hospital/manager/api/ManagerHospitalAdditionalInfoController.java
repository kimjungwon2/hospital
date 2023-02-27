package site.hospital.hospital.manager.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.hospital.manager.api.dto.StaffModifyStaffHosRequest;
import site.hospital.hospital.manager.service.ManagerHospitalAdditionalInfoService;
import site.hospital.hospital.user.api.dto.additionalinfo.HospitalAdditionalInfoViewResponse;
import site.hospital.hospital.user.service.HospitalAdditionalInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerHospitalAdditionalInfoController {

    private final HospitalAdditionalInfoService hospitalAdditionalInfoService;
    private final ManagerHospitalAdditionalInfoService managerHospitalAdditionalInfoService;

    @GetMapping("/staff/staffHosInfo/{staffHosId}")
    public HospitalAdditionalInfoViewResponse mangerViewHosAdditionalInfo(
            @PathVariable("staffHosId") Long staffHosId) {
        return hospitalAdditionalInfoService.viewHospitalAdditionalInfo(staffHosId);
    }

    @PutMapping("/staff/staffHosInfo/modify/{staffHosId}")
    public void managerModifyHosAdditionalInfo(
            ServletRequest servletRequest,
            @PathVariable("staffHosId") Long staffHosId,
            @RequestBody @Validated StaffModifyStaffHosRequest request
    ) {
        managerHospitalAdditionalInfoService.modifyHospitalAdditionalInfo(servletRequest, staffHosId, request);
    }

    //수정 예정
    @DeleteMapping("/staff/{memberId}/staffHosInfo/delete/{staffHosId}")
    public void managerDeleteHosAdditionalInfo(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("staffHosId") Long staffHosId
    ) {
        managerHospitalAdditionalInfoService.deleteHospitalAdditionalInfo(servletRequest, staffHosId);
    }
}
