package site.hospital.common.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;
import site.hospital.review.user.domain.ReviewImage;
import site.hospital.review.user.repository.ReviewImageRepository;
import site.hospital.review.user.repository.ReviewRepository;

@Service
public class ReviewReceiptImageService extends ImageManagementService{

    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;
    private static final String DIR_NAME = "receipt";

    public ReviewReceiptImageService(
            AmazonS3Client amazonS3Client,
            ReviewImageRepository reviewImageRepository,
            ReviewRepository reviewRepository
    ) {
        super(amazonS3Client);
        this.reviewImageRepository = reviewImageRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public String uploadImage(
            MultipartFile multipartFile,
            Long reviewId
    ) throws IOException {
        File uploadImageFile = uploadLocal(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException(
                        "error: MultipartFile -> File convert fail"));

        return uploadReviewReceiptImage(uploadImageFile, reviewId);
    }

    @Override
    public void deleteImage(Long imageId) {
        ReviewImage reviewImage = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalStateException("등록되지 않은 영수증 사진."));

        String imageKey = reviewImage.getImageKey();
        deleteS3Images(DIR_NAME, imageKey);
        deleteReviewReceiptImage(imageId);
    }

    @Transactional
    protected Long registerReviewImage(
            ReviewImage reviewImage
            , Long reviewId
    ) {
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        checkReviewReceiptImage(review);
        registerReviewReceiptImage(reviewImage, review);

        return reviewImage.getId();
    }

    @Transactional
    protected void deleteReviewReceiptImage(Long reviewImageId) {
        reviewRepository.deleteById(reviewImageId);
    }

    private void checkReviewReceiptImage(Review review) {
        if (review.getReviewImage() != null) {
            throw new IllegalStateException("이미 리뷰 영수증 인증이 있습니다.");
        }
    }

    private void registerReviewReceiptImage(ReviewImage reviewImage, Review review) {
        review.changeReviewImage(reviewImage);
        review.changeAuthenticationStatus(ReviewAuthentication.WAITING);
        reviewImageRepository.save(reviewImage);
    }

    private String uploadReviewReceiptImage(File uploadFile, Long reviewId)
            throws IOException{

        String extension = confirmImageExtension(uploadFile);

        String imageName = createUUIDName(DIR_NAME, extension);
        String uploadImageUrl = putS3(uploadFile, imageName);
        removeLocalImage(uploadFile);

        String imageKey = getImageKey(DIR_NAME, imageName);


        ReviewImage reviewImage =
                ReviewImage
                        .builder()
                        .originalName(uploadFile.getName())
                        .imageKey(imageKey)
                        .build();

        registerReviewImage(reviewImage, reviewId);

        return uploadImageUrl;
    }

}
