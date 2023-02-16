package site.hospital.review.manager.service;


import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtAccessService;
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.repository.ReviewRepository;
import site.hospital.review.user.repository.dto.StaffReviewSearchCondition;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerReviewService {

    private final ManagerJwtAccessService managerJwtAccessService;
    private final ReviewRepository reviewRepository;

    //병원 관계자 리뷰 검색
    public Page<ReviewSearchListsResponse> staffSearchReviews(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        StaffReviewSearchCondition condition = StaffReviewSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).build();

        Long hospitalId = managerJwtAccessService.getHospitalNumber(servletRequest);
        Page<Review> reviews = reviewRepository.staffSearchReviews(hospitalId, condition, pageable);

        List<ReviewSearchListsResponse> result = reviews.stream()
                .map(r -> ReviewSearchListsResponse.from(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl(result, pageable, total);
    }



}
