package site.hospital.dto.staffHosInfo;

import lombok.Data;

@Data
public class AdminModifyStaffHosRequest{
    private Long hospitalId;
    private String introduction;
    private String consultationHour;
    private String abnormality;
}