package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Doctor;
import site.hospital.dto.doctor.CreateDoctorRequest;
import site.hospital.dto.doctor.StaffCreateDoctorRequest;
import site.hospital.service.DoctorService;

import javax.servlet.ServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class DoctorApiController {

    private final DoctorService doctorService;

    //병원 관계자 의사 등록
    @PostMapping("/staff/doctor/register")
    public CreateDoctorResponse staffSaveDoctor(ServletRequest servletRequest,
            @RequestBody @Validated StaffCreateDoctorRequest request) {
        Long id = doctorService.staffCreateDoctor(servletRequest, request);
        return new CreateDoctorResponse(id);
    }

    //병원 관계자 의사 수정
    @PutMapping("/staff/doctor/modify/{doctorId}")
    public void staffModifyDoctor(ServletRequest servletRequest,
            @PathVariable("doctorId") Long doctorId,
            @RequestBody @Validated StaffModifyDoctorRequest request) {
        Doctor doctor = Doctor.builder()
                .history(request.getHistory())
                .name(request.getName()).build();

        doctorService.staffModifyDoctor(servletRequest, request.getMemberId(), doctorId, doctor);
    }

    //병원 관계자 의사 삭제
    @DeleteMapping("/staff/{memberId}/doctor/delete/{doctorId}")
    public void staffDeleteDoctor(ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId, @PathVariable("doctorId") Long doctorId) {
        doctorService.staffDeleteDoctor(servletRequest, memberId, doctorId);
    }

    @PostMapping("/admin/doctor/register")
    public CreateDoctorResponse saveDoctor(@RequestBody @Validated CreateDoctorRequest request) {
        Long id = doctorService.createDoctor(request);
        return new CreateDoctorResponse(id);
    }

    @PutMapping("/admin/doctor/modify/{doctorId}")
    public void modifyDoctor(@PathVariable("doctorId") Long doctorId,
            @RequestBody @Validated AdminModifyDoctorRequest request) {
        Doctor doctor = Doctor.builder()
                .history(request.getHistory())
                .name(request.getName()).build();

        doctorService.modifyDoctor(doctorId, doctor);
    }

    @DeleteMapping("/admin/doctor/delete/{doctorId}")
    public void deleteDoctor(@PathVariable("doctorId") Long doctorId) {
        doctorService.deleteDoctor(doctorId);
    }


    @Data
    private static class CreateDoctorResponse {

        Long DoctorId;

        public CreateDoctorResponse(Long DoctorId) {
            this.DoctorId = DoctorId;
        }
    }

    @Data
    private static class AdminModifyDoctorRequest {

        @NotNull(message = "의사 경력을 입력해주세요.")
        private String history;
        @NotNull(message = "의사 이름을 입력해주세요.")
        private String name;
    }

    @Data
    private static class StaffModifyDoctorRequest {

        private Long memberId;
        private String history;
        private String name;
    }
}
