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
import site.hospital.bookmark.user.repository.dto.StaffBookmarkSearchCondition;
import site.hospital.common.service.ManagerJwtAccessService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerBookmarkService {

    private final ManagerJwtAccessService managerJwtAccessService;
    private final BookmarkRepository bookmarkRepository;

    //병원 관계자 즐겨찾기 검색
    public Page<Bookmark> staffSearchBookmarkUsers(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            String phoneNumber,
            Pageable pageable
    ) {
        StaffBookmarkSearchCondition condition =
                StaffBookmarkSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .phoneNumber(phoneNumber)
                        .build();

        Long JwtHospitalId = managerJwtAccessService.getHospitalNumber(servletRequest);

        Page<Bookmark> bookmarks = bookmarkRepository
                .staffSearchBookmark(JwtHospitalId, condition, pageable);

        List<BookmarkAdminSearchMemberResponse> content =
                bookmarks.stream()
                        .map(b -> BookmarkAdminSearchMemberResponse.from(b))
                        .collect(Collectors.toList());

        Long total = bookmarks.getTotalElements();

        return new PageImpl(content, pageable, total);
    }
}
