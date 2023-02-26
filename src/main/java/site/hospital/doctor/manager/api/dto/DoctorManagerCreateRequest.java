package site.hospital.doctor.manager.api.dto;


import lombok.Data;

@Data
public class DoctorManagerCreateRequest {

    private Long hospitalAdditionalInfoId;
    private String name;
    private String history;
    private Long memberId;
}
