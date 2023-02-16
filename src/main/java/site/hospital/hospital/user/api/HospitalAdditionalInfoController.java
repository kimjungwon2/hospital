package site.hospital.hospital.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.hospital.user.api.dto.staffHospital.StaffHospitalViewResponse;
import site.hospital.hospital.user.service.HospitalAdditionalInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HospitalAdditionalInfoController {

    private final HospitalAdditionalInfoService hospitalAdditionalInfoService;


    //병원 추가 정보 보기(고객)
    @GetMapping("/hospital/staffHosInfo/{staffHosId}")
    public StaffHospitalViewResponse viewStaffHosInfo(
            @PathVariable("staffHosId") Long staffHosId
    ) {
        return hospitalAdditionalInfoService.viewStaffHosInfo(staffHosId);
    }

}
