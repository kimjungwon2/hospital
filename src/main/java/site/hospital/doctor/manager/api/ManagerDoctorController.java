package site.hospital.doctor.manager.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.doctor.manager.api.dto.DoctorCreateResponse;
import site.hospital.doctor.manager.api.dto.DoctorManagerModifyRequest;
import site.hospital.doctor.manager.api.dto.DoctorManagerCreateRequest;
import site.hospital.doctor.manager.service.ManagerDoctorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerDoctorController {

    private final ManagerDoctorService managerDoctorService;

    @PostMapping("/staff/doctor/register")
    public DoctorCreateResponse managerRegisterDoctor(
            ServletRequest servletRequest,
            @RequestBody @Validated DoctorManagerCreateRequest request
    ) {
        return managerDoctorService.createDoctor(servletRequest, request);
    }

    @PutMapping("/staff/doctor/modify/{doctorId}")
    public void managerModifyDoctor(
            ServletRequest servletRequest,
            @PathVariable("doctorId") Long doctorId,
            @RequestBody @Validated DoctorManagerModifyRequest request
    ) {
        managerDoctorService.modifyDoctor(doctorId,servletRequest, request);
    }

    @DeleteMapping("/staff/{memberId}/doctor/delete/{doctorId}")
    public void managerDeleteDoctor(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("doctorId") Long doctorId
    ) {
        managerDoctorService.deleteDoctor(servletRequest, memberId, doctorId);
    }


}
