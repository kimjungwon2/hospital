package site.hospital.bookmark.admin.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.bookmark.user.domain.Bookmark;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class BookmarkAdminSearchMemberResponse {

    private final Long bookmarkId;
    private final String memberIdName;
    private final String nickName;
    private final String phoneNumber;

    public static BookmarkAdminSearchMemberResponse from(Bookmark bookmark) {
        Assert.notNull(bookmark.getMember(),"member must be provided");

        return BookmarkAdminSearchMemberResponse
                .builder()
                .bookmarkId(bookmark.getId())
                .memberIdName(bookmark.getMember().getMemberIdName())
                .nickName(bookmark.getMember().getNickName())
                .phoneNumber(bookmark.getMember().getPhoneNumber())
                .build();
    }
}
