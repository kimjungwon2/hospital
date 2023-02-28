package site.hospital.bookmark.manager.service;


import javax.servlet.ServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.bookmark.user.domain.Bookmark;

public interface ManagerBookmarkService {

    Page<Bookmark> managerSearchBookmarkUsers(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            String phoneNumber,
            Pageable pageable
    );

}
