package site.hospital.hospital.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.hospital.user.api.dto.additionalinfo.HospitalAdditionalInfoViewResponse;
import site.hospital.hospital.user.service.HospitalAdditionalInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HospitalAdditionalInfoController {

    private final HospitalAdditionalInfoService hospitalAdditionalInfoService;

    @GetMapping("/hospital/staffHosInfo/{staffHosId}")
    public HospitalAdditionalInfoViewResponse viewHospitalAdditionalInfo(
            @PathVariable("staffHosId") Long staffHosId
    ) {
        return hospitalAdditionalInfoService.viewHospitalAdditionalInfo(staffHosId);
    }

}
