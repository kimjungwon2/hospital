package site.hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class StaffReviewSearchCondition {

    private String nickName;
    private String memberIdName;

    @Builder
    public StaffReviewSearchCondition(String nickName, String memberIdName) {
        this.nickName = nickName;
        this.memberIdName = memberIdName;
    }
}
