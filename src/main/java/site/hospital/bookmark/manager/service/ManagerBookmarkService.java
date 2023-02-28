package site.hospital.bookmark.manager.service;


import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.bookmark.user.api.dto.BookmarkAdminSearchMemberResponse;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.bookmark.user.repository.BookmarkRepository;
import site.hospital.bookmark.user.repository.dto.ManagerBookmarkSearchCondition;
import site.hospital.common.service.ManagerJwtService;

public interface ManagerBookmarkService {

    Page<Bookmark> managerSearchBookmarkUsers(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            String phoneNumber,
            Pageable pageable
    );

}
