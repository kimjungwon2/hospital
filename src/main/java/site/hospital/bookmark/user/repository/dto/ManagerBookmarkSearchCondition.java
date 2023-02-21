package site.hospital.bookmark.user.repository.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ManagerBookmarkSearchCondition {

    private String nickName;
    private String memberIdName;
    private String phoneNumber;

    @Builder
    public ManagerBookmarkSearchCondition(String nickName, String phoneNumber, String memberIdName) {
        this.nickName = nickName;
        this.memberIdName = memberIdName;
        this.phoneNumber = phoneNumber;
    }
}
