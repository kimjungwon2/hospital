package site.hospital.review.manager.repository.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ManagerReviewSearchCondition {

    private String nickName;
    private String memberIdName;

    @Builder
    public ManagerReviewSearchCondition(String nickName, String memberIdName) {
        this.nickName = nickName;
        this.memberIdName = memberIdName;
    }
}
