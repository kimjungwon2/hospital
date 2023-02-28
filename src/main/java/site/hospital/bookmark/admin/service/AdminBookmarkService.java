package site.hospital.bookmark.admin.service;


import java.util.List;
import site.hospital.bookmark.user.api.dto.BookmarkSearchResponse;


public interface AdminBookmarkService {

    List<BookmarkSearchResponse> adminSearchBookmarkUsers();
}
