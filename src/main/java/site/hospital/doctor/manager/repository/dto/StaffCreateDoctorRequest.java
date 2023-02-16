package site.hospital.doctor.manager.repository.dto;


import lombok.Data;

@Data
public class StaffCreateDoctorRequest {

    private Long staffHosId;
    private String name;
    private String history;
    private Long memberId;
}
