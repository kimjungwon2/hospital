package site.hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class StaffBookmarkSearchCondition {

    private String nickName;
    private String memberIdName;
    private String phoneNumber;

    @Builder
    public StaffBookmarkSearchCondition(String nickName, String phoneNumber, String memberIdName) {
        this.nickName = nickName;
        this.memberIdName = memberIdName;
        this.phoneNumber = phoneNumber;
    }
}
