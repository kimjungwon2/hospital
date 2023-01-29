package site.hospital.api.dto.doctor;

import lombok.Data;

@Data
public class DoctorStaffModifyRequest {

    private Long memberId;
    private String history;
    private String name;

}
