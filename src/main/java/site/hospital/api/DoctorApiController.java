package site.hospital.api;

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
import site.hospital.api.dto.doctor.DoctorAdminModifyRequest;
import site.hospital.api.dto.doctor.DoctorCreateResponse;
import site.hospital.api.dto.doctor.DoctorStaffModifyRequest;
import site.hospital.domain.Doctor;
import site.hospital.dto.doctor.CreateDoctorRequest;
import site.hospital.dto.doctor.StaffCreateDoctorRequest;
import site.hospital.service.DoctorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DoctorApiController {

    private final DoctorService doctorService;

    //병원 관계자 의사 등록
    @PostMapping("/staff/doctor/register")
    public DoctorCreateResponse staffSaveDoctor(
            ServletRequest servletRequest,
            @RequestBody @Validated StaffCreateDoctorRequest request
    ) {
        return doctorService.staffCreateDoctor(servletRequest, request);
    }

    //병원 관계자 의사 수정
    @PutMapping("/staff/doctor/modify/{doctorId}")
    public void staffModifyDoctor(
            ServletRequest servletRequest,
            @PathVariable("doctorId") Long doctorId,
            @RequestBody @Validated DoctorStaffModifyRequest request
    ) {
        doctorService.staffModifyDoctor(servletRequest, request.getMemberId(), doctorId, request);
    }

    //병원 관계자 의사 삭제
    @DeleteMapping("/staff/{memberId}/doctor/delete/{doctorId}")
    public void staffDeleteDoctor(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("doctorId") Long doctorId
    ) {
        doctorService.staffDeleteDoctor(servletRequest, memberId, doctorId);
    }

    @PostMapping("/admin/doctor/register")
    public DoctorCreateResponse saveDoctor(@RequestBody @Validated CreateDoctorRequest request) {
        return doctorService.createDoctor(request);
    }

    @PutMapping("/admin/doctor/modify/{doctorId}")
    public void modifyDoctor(
            @PathVariable("doctorId") Long doctorId,
            @RequestBody @Validated DoctorAdminModifyRequest request
    ) {
        doctorService.modifyDoctor(doctorId, request);
    }

    @DeleteMapping("/admin/doctor/delete/{doctorId}")
    public void deleteDoctor(@PathVariable("doctorId") Long doctorId) {
        doctorService.deleteDoctor(doctorId);
    }

}
