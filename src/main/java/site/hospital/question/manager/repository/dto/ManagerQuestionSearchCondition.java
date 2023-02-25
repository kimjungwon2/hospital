package site.hospital.question.manager.repository.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ManagerQuestionSearchCondition {

    private String nickName;
    private String memberIdName;

    @Builder
    public ManagerQuestionSearchCondition(String nickName, String memberIdName) {
        this.nickName = nickName;
        this.memberIdName = memberIdName;
    }
}
