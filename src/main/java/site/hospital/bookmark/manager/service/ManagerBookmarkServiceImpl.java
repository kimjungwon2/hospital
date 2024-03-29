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
import site.hospital.bookmark.admin.api.dto.BookmarkAdminSearchMemberResponse;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.bookmark.user.repository.BookmarkRepository;
import site.hospital.bookmark.user.repository.dto.ManagerBookmarkSearchCondition;
import site.hospital.common.service.ManagerJwtService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerBookmarkServiceImpl implements ManagerBookmarkService {

    private final ManagerJwtService managerJwtService;
    private final BookmarkRepository bookmarkRepository;

    @Override
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
                        .map(BookmarkAdminSearchMemberResponse::from)
                        .collect(Collectors.toList());

        Long totalAmount = managerSearchBookmarkUsers.getTotalElements();

        return new PageImpl(content, pageable, totalAmount);
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

        Long hospitalId = managerJwtService.getHospitalNumber(servletRequest);

        return bookmarkRepository
                .managerSearchBookmarkUsers(hospitalId, searchCondition, pageable);
    }
}
