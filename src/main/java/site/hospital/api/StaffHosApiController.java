package site.hospital.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.Doctor;
import site.hospital.domain.StaffHosInformation;
import site.hospital.dto.hospital.staff.StaffModifyStaffHosRequest;
import site.hospital.dto.staffHosInfo.AdminModifyStaffHosRequest;
import site.hospital.service.StaffHosService;

@RestController
@RequiredArgsConstructor
public class StaffHosApiController {

    private final StaffHosService staffHosService;


    //병원 추가 정보 보기(고객)
    @GetMapping("/hospital/staffHosInfo/{staffHosId}")
    public StaffHosInfoView viewStaffHosInfo(@PathVariable("staffHosId") Long staffHosId) {
        StaffHosInformation staffHosInformation = staffHosService.viewStaffHosInfo(staffHosId);
        StaffHosInfoView staffHosInfoView = new StaffHosInfoView(staffHosInformation);

        return staffHosInfoView;
    }

    //병원 관계자 추가 정보 보기
    @GetMapping("/staff/staffHosInfo/{staffHosId}")
    public StaffHosInfoView staffViewStaffHosInfo(@PathVariable("staffHosId") Long staffHosId) {
        StaffHosInformation staffHosInformation = staffHosService.viewStaffHosInfo(staffHosId);
        StaffHosInfoView staffHosInfoView = new StaffHosInfoView(staffHosInformation);

        return staffHosInfoView;
    }

    //병원 관계자 추가 정보 수정하기
    @PutMapping("/staff/staffHosInfo/modify/{staffHosId}")
    public void staffModifyStaffHosInfo(ServletRequest servletRequest,
            @PathVariable("staffHosId") Long staffHosId,
            @RequestBody @Validated StaffModifyStaffHosRequest request) {
        staffHosService.staffModifyStaffHosInfo(servletRequest, staffHosId, request);
    }

    //병원 관계자 추가 정보 삭제하기
    @DeleteMapping("/staff/{memberId}/staffHosInfo/delete/{staffHosId}")
    public void staffDeleteStaffHosInfo(ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId, @PathVariable("staffHosId") Long staffHosId) {
        staffHosService.staffDeleteStaffHosInfo(servletRequest, memberId, staffHosId);
    }

    //관리자 병원 추가 정보 보기
    @GetMapping("/admin/staffHosInfo/{staffHosId}")
    public StaffHosInfoView adminViewStaffHosInfo(@PathVariable("staffHosId") Long staffHosId) {
        StaffHosInformation staffHosInformation = staffHosService.viewStaffHosInfo(staffHosId);
        StaffHosInfoView staffHosInfoView = new StaffHosInfoView(staffHosInformation);

        return staffHosInfoView;
    }

    //관리자 추가 정보 수정하기
    @PutMapping("/admin/staffHosInfo/modify/{staffHosId}")
    public void modifyStaffHosInfo(@PathVariable("staffHosId") Long staffHosId,
            @RequestBody @Validated AdminModifyStaffHosRequest request) {
        staffHosService.adminModifyStaffHosInfo(staffHosId, request);
    }

    //관리자 추가 정보 삭제하기
    @DeleteMapping("/admin/staffHosInfo/delete/{staffHosId}")
    public void deleteStaffHosInfo(@PathVariable("staffHosId") Long staffHosId) {
        staffHosService.adminDeleteStaffHosInfo(staffHosId);
    }

    /*DTO*/
    @Data
    private static class StaffHosInfoView {

        private String introduction;
        private String consultationHour;
        private String abnormality;
        private List<DoctorDTO> doctors;

        public StaffHosInfoView(StaffHosInformation staffHosInformation) {
            this.introduction = staffHosInformation.getIntroduction();
            this.consultationHour = staffHosInformation.getConsultationHour();
            this.abnormality = staffHosInformation.getAbnormality();
            this.doctors = staffHosInformation.getDoctors().stream()
                    .map(d -> new DoctorDTO(d))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class DoctorDTO {

        private Long doctorId;
        private String name;
        private String history;

        public DoctorDTO(Doctor doctor) {
            this.doctorId = doctor.getId();
            this.name = doctor.getName();
            this.history = doctor.getHistory();
        }
    }

}
