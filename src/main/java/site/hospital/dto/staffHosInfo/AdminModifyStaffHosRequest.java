package site.hospital.dto.staffHosInfo;

import lombok.Data;

@Data
public class AdminModifyStaffHosRequest{
    private String introduction;
    private String consultationHour;
    private String abnormality;
}