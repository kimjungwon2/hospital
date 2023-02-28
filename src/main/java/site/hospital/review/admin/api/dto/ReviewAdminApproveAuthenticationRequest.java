package site.hospital.review.admin.api.dto;

import lombok.Data;
import site.hospital.review.user.domain.ReviewAuthentication;

@Data
public class ReviewAdminApproveAuthenticationRequest {

    private ReviewAuthentication reviewAuthentication;

}
