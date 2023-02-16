package site.hospital.doctor.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.doctor.manager.api.dto.DoctorAdminModifyRequest;
import site.hospital.doctor.manager.api.dto.DoctorCreateResponse;
import site.hospital.doctor.manager.repository.dto.CreateDoctorRequest;
import site.hospital.doctor.manager.service.DoctorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminDoctorController {

    private final DoctorService doctorService;

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
