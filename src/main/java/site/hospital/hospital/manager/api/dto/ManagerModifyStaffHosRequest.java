package site.hospital.hospital.manager.api.dto;

import lombok.Data;

@Data
public class ManagerModifyStaffHosRequest {

    private Long memberId;
    private String introduction;
    private String consultationHour;
    private String abnormality;
}
