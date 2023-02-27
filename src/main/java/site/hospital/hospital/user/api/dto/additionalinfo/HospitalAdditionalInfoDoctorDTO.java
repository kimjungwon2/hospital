package site.hospital.hospital.user.api.dto.additionalinfo;

import lombok.Data;
import site.hospital.doctor.manager.domain.Doctor;

@Data
public class HospitalAdditionalInfoDoctorDTO {

    private Long doctorId;
    private String name;
    private String history;

    public HospitalAdditionalInfoDoctorDTO(Doctor doctor) {
        this.doctorId = doctor.getId();
        this.name = doctor.getName();
        this.history = doctor.getHistory();
    }
}
