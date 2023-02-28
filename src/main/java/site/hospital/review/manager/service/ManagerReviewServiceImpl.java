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
import site.hospital.common.service.ManagerJwtService;
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.repository.ReviewRepository;
import site.hospital.review.manager.repository.dto.ManagerReviewSearchCondition;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerReviewServiceImpl implements ManagerReviewService {

    private final ManagerJwtService managerJwtService;
    private final ReviewRepository reviewRepository;

    @Override
    public Page<ReviewSearchListsResponse> managerSearchReviews(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        Long hospitalId = managerJwtService.getHospitalNumber(servletRequest);

        Page<Review> reviews = getSearchReviewResults(nickName, memberIdName, pageable, hospitalId);

        List<ReviewSearchListsResponse> searchResults =
                reviews
                        .stream()
                        .map(r -> ReviewSearchListsResponse.from(r))
                        .collect(Collectors.toList());

        Long totalCounts = reviews.getTotalElements();

        return new PageImpl(searchResults, pageable, totalCounts);
    }

    private Page<Review> getSearchReviewResults(
            String nickName,
            String memberIdName,
            Pageable pageable,
            Long hospitalId
    ) {
        ManagerReviewSearchCondition searchCondition =
                ManagerReviewSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .build();

        Page<Review> reviews = reviewRepository.managerSearchReviews(hospitalId, searchCondition, pageable);

        return reviews;
    }

}
