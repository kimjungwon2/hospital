package site.hospital.review.admin.repository.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminReviewSearchCondition {

    private String nickName;
    private String hospitalName;
    private String memberIdName;

    @Builder
    public AdminReviewSearchCondition(String nickName, String hospitalName, String memberIdName) {
        this.nickName = nickName;
        this.hospitalName = hospitalName;
        this.memberIdName = memberIdName;
    }
}
