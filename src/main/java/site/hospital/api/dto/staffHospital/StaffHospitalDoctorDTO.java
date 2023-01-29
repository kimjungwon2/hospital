package site.hospital.api.dto.staffHospital;

import lombok.Data;
import site.hospital.domain.Doctor;

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
