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
import site.hospital.doctor.manager.api.dto.DoctorStaffModifyRequest;
import site.hospital.doctor.manager.repository.dto.StaffCreateDoctorRequest;
import site.hospital.doctor.manager.service.DoctorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerDoctorController {

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


}
