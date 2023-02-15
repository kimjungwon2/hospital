package site.hospital.question.repository.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminQuestionSearchCondition {

    private String nickName;
    private String hospitalName;
    private String memberIdName;

    @Builder
    public AdminQuestionSearchCondition(String nickName, String hospitalName, String memberIdName) {
        this.nickName = nickName;
        this.hospitalName = hospitalName;
        this.memberIdName = memberIdName;
    }
}
