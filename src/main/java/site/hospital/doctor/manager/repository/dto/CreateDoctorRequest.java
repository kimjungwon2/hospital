package site.hospital.doctor.manager.repository.dto;

import lombok.Data;

@Data
public class CreateDoctorRequest {

    private Long staffHosId;
    private String name;
    private String history;
}
