package site.hospital.bookmark.user.service;

import java.util.List;
import site.hospital.bookmark.user.api.dto.BookmarkCheckResponse;
import site.hospital.bookmark.user.api.dto.BookmarkCreateRequest;
import site.hospital.bookmark.user.api.dto.BookmarkSearchMemberResponse;

public interface BookmarkService {


    BookmarkCheckResponse userCheckBookmark(Long memberId, Long hospitalId);

    void userRegisterBookmark(BookmarkCreateRequest request);

    List<BookmarkSearchMemberResponse> searchUserBookmarks(Long memberId);
}
