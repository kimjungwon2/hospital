package site.hospital.review.manager.service;


import javax.servlet.ServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;

public interface ManagerReviewService {

    Page<ReviewSearchListsResponse> managerSearchReviews(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    );

}
