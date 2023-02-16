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
import site.hospital.hospital.manager.repository.dto.StaffModifyStaffHosRequest;
import site.hospital.hospital.manager.service.ManagerHospitalAdditionalInfoService;
import site.hospital.hospital.user.api.dto.staffHospital.StaffHospitalViewResponse;
import site.hospital.hospital.user.service.HospitalAdditionalInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerHospitalAdditionalInfoController {

    private final HospitalAdditionalInfoService hospitalAdditionalInfoService;
    private final ManagerHospitalAdditionalInfoService managerHospitalAdditionalInfoService;

    //병원 관계자 추가 정보 보기
    @GetMapping("/staff/staffHosInfo/{staffHosId}")
    public StaffHospitalViewResponse staffViewStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId) {
        return hospitalAdditionalInfoService.viewStaffHosInfo(staffHosId);
    }

    //병원 관계자 추가 정보 수정하기
    @PutMapping("/staff/staffHosInfo/modify/{staffHosId}")
    public void staffModifyStaffHosInfo(
            ServletRequest servletRequest,
            @PathVariable("staffHosId") Long staffHosId,
            @RequestBody @Validated StaffModifyStaffHosRequest request
    ) {
        managerHospitalAdditionalInfoService.staffModifyStaffHosInfo(servletRequest, staffHosId, request);
    }

    //병원 관계자 추가 정보 삭제하기
    @DeleteMapping("/staff/{memberId}/staffHosInfo/delete/{staffHosId}")
    public void staffDeleteStaffHosInfo(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("staffHosId") Long staffHosId
    ) {
        managerHospitalAdditionalInfoService.staffDeleteStaffHosInfo(servletRequest, memberId, staffHosId);
    }
}
