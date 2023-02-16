package site.hospital.bookmark.user.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class BookmarkCheckResponse {

    private final Boolean isBookmark;

    public static BookmarkCheckResponse from(Boolean isBookmark) {
        return BookmarkCheckResponse
                .builder()
                .isBookmark(isBookmark)
                .build();
    }

}
