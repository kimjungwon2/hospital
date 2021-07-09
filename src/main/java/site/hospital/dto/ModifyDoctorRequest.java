package site.hospital.dto;

import lombok.Data;

@Data
public class ModifyDoctorRequest {
    private Long doctorId;
    private String name;
    private String history;
    private String photo;

}
