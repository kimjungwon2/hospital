package site.hospital.dto.hospital.staff;

import lombok.Data;

@Data
public class StaffModifyStaffHosRequest {
    private Long memberId;
    private String introduction;
    private String consultationHour;
    private String abnormality;
}
