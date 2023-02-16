package site.hospital.doctor.manager.repository.dto;

import lombok.Data;

@Data
public class ModifyDoctorRequest {

    private Long doctorId;
    private String name;
    private String history;

}
