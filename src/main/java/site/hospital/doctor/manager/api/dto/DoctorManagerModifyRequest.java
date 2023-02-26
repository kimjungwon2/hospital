package site.hospital.doctor.manager.api.dto;

import lombok.Data;

@Data
public class DoctorManagerModifyRequest {

    private Long memberId;
    private String history;
    private String name;

}
