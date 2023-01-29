package site.hospital.api.dto.review;

import lombok.Data;
import site.hospital.domain.review.ReviewAuthentication;

@Data
public class ReviewAdminApproveAuthenticationRequest {

    private ReviewAuthentication reviewAuthentication;

}
