package site.hospital.hospital.user.api.dto.staffHospital;

import lombok.Data;
import site.hospital.doctor.domain.Doctor;

@Data
public class StaffHospitalDoctorDTO {

    private Long doctorId;
    private String name;
    private String history;

    public StaffHospitalDoctorDTO(Doctor doctor) {
        this.doctorId = doctor.getId();
        this.name = doctor.getName();
        this.history = doctor.getHistory();
    }
}
