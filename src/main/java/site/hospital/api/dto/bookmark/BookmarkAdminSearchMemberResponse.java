package site.hospital.api.dto.bookmark;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.Bookmark;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class BookmarkAdminSearchMemberResponse {

    private Long bookmarkId;
    private String memberIdName;
    private String nickName;
    private String phoneNumber;

    public static BookmarkAdminSearchMemberResponse from(Bookmark bookmark) {
        return BookmarkAdminSearchMemberResponse
                .builder()
                .bookmarkId(bookmark.getId())
                .memberIdName(bookmark.getMember().getMemberIdName())
                .nickName(bookmark.getMember().getNickName())
                .phoneNumber(bookmark.getMember().getPhoneNumber())
                .build();
    }
}
