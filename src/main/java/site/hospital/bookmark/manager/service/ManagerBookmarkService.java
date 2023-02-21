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
import site.hospital.common.service.ManagerJwtAccessService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerBookmarkService {

    private final ManagerJwtAccessService managerJwtAccessService;
    private final BookmarkRepository bookmarkRepository;

    public Page<Bookmark> managerSearchBookmarkUsers(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            String phoneNumber,
            Pageable pageable
    ) {
        Page<Bookmark> managerSearchBookmarkUsers = getManagerSearchBookmarkUsers(
                                                servletRequest,
                                                nickName,
                                                memberIdName,
                                                phoneNumber,
                                                pageable);

        List<BookmarkAdminSearchMemberResponse> content =
                        managerSearchBookmarkUsers
                        .stream()
                        .map(b -> BookmarkAdminSearchMemberResponse.from(b))
                        .collect(Collectors.toList());

        Long totalPage = managerSearchBookmarkUsers.getTotalElements();

        return new PageImpl(content, pageable, totalPage);
    }

    private Page<Bookmark> getManagerSearchBookmarkUsers(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            String phoneNumber,
            Pageable pageable
    ) {
        ManagerBookmarkSearchCondition searchCondition =
                ManagerBookmarkSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .phoneNumber(phoneNumber)
                        .build();

        Long JwtHospitalId = managerJwtAccessService.getHospitalNumber(servletRequest);

        Page<Bookmark> findSearchBookmarkUsers = bookmarkRepository
                .managerSearchBookmarkUsers(JwtHospitalId, searchCondition, pageable);

        return findSearchBookmarkUsers;
    }
}
