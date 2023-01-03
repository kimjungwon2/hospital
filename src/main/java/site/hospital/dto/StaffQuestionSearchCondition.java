package site.hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class StaffQuestionSearchCondition {

    private String nickName;
    private String memberIdName;

    @Builder
    public StaffQuestionSearchCondition(String nickName, String memberIdName) {
        this.nickName = nickName;
        this.memberIdName = memberIdName;
    }
}
