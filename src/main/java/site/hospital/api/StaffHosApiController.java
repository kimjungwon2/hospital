package site.hospital.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.staffHospital.StaffHospitalViewResponse;
import site.hospital.dto.hospital.staff.StaffModifyStaffHosRequest;
import site.hospital.dto.staffHosInfo.AdminModifyStaffHosRequest;
import site.hospital.service.StaffHosService;

@RestController
@RequiredArgsConstructor
public class StaffHosApiController {

    private final StaffHosService staffHosService;


    //병원 추가 정보 보기(고객)
    @GetMapping("/hospital/staffHosInfo/{staffHosId}")
    public StaffHospitalViewResponse viewStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId
    ) {
        return staffHosService.viewStaffHosInfo(staffHosId);
    }

    //병원 관계자 추가 정보 보기
    @GetMapping("/staff/staffHosInfo/{staffHosId}")
    public StaffHospitalViewResponse staffViewStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId) {
        return staffHosService.viewStaffHosInfo(staffHosId);
    }

    //병원 관계자 추가 정보 수정하기
    @PutMapping("/staff/staffHosInfo/modify/{staffHosId}")
    public void staffModifyStaffHosInfo(
            ServletRequest servletRequest,
            @PathVariable("staffHosId") Long staffHosId,
            @RequestBody @Validated StaffModifyStaffHosRequest request
    ) {
        staffHosService.staffModifyStaffHosInfo(servletRequest, staffHosId, request);
    }

    //병원 관계자 추가 정보 삭제하기
    @DeleteMapping("/staff/{memberId}/staffHosInfo/delete/{staffHosId}")
    public void staffDeleteStaffHosInfo(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("staffHosId") Long staffHosId
    ) {
        staffHosService.staffDeleteStaffHosInfo(servletRequest, memberId, staffHosId);
    }

    //관리자 병원 추가 정보 보기
    @GetMapping("/admin/staffHosInfo/{staffHosId}")
    public StaffHospitalViewResponse adminViewStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId) {
        return staffHosService.viewStaffHosInfo(staffHosId);
    }

    //관리자 추가 정보 수정하기
    @PutMapping("/admin/staffHosInfo/modify/{staffHosId}")
    public void modifyStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId,
            @RequestBody @Validated AdminModifyStaffHosRequest request
    ) {
        staffHosService.adminModifyStaffHosInfo(staffHosId, request);
    }

    //관리자 추가 정보 삭제하기
    @DeleteMapping("/admin/staffHosInfo/delete/{staffHosId}")
    public void deleteStaffHosInfo(@PathVariable("staffHosId") Long staffHosId) {
        staffHosService.adminDeleteStaffHosInfo(staffHosId);
    }

}
