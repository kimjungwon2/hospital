package site.hospital.common.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;
import site.hospital.review.user.domain.ReviewImage;
import site.hospital.review.user.repository.ReviewImageRepository;
import site.hospital.review.user.repository.ReviewRepository;

@Slf4j
@Service
public class ReviewReceiptImageService extends ImageManagementService{

    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;

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
            String dirName,
            Long reviewId
    ) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException(
                        "error: MultipartFile -> File convert fail"));

        return reviewImageUpload(uploadFile, dirName, reviewId);
    }

    @Override
    public void deleteImage(Long imageId, String dirName) {
        ReviewImage reviewImage = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalStateException("등록되지 않은 영수증 사진."));

        //이미지 키 찾기
        String imageKey = reviewImage.getImageKey();
        String[] fileName = new String[3];

        //3개의 파일 삭제
        fileName[0] = dirName + "/" + imageKey; // 기존 폴더
        fileName[1] = "w140" + "/" + imageKey; //w140 폴더
        fileName[2] = "w600" + "/" + imageKey; //w600 폴더

        //S3 이미지 연속해서 삭제.
        for (int i = 0; i < 3; i++) {
            DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName[i]);
            amazonS3Client.deleteObject(request);
        }

        //이미지 DB 삭제
        deleteReviewImage(imageId);
    }

    // S3로 리뷰 영수증 업로드
    private String reviewImageUpload(File uploadFile, String dirName, Long reviewId) {

        //확장자 찾기
        String uploadName = uploadFile.getName();
        String extension = uploadName.substring(uploadName.lastIndexOf(".") + 1);
        extension = extension.toLowerCase();

        //이미지 파일 확장자가 아닌 경우 exception 발생.
        if (!extension.equals("bmp") && !extension.equals("rle") && !extension.equals("dib")
                && !extension.equals("jpeg") && !extension.equals("jpg")
                && !extension.equals("png") && !extension.equals("gif")
                && !extension.equals("jfif") && !extension.equals("tif")
                && !extension.equals("tiff") && !extension.equals("raw")) {
            throw new IllegalStateException("이미지 확장자가 아닙니다.");
        }

        String fileName = dirName + "/" + UUID.randomUUID() + "." + extension; // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);

        String key = fileName.replace(dirName + "/", ""); // 키 값 저장.

        //DB에 정보 저장.
        ReviewImage reviewImage = ReviewImage.builder()
                .originalName(uploadFile.getName()) // 파일 원본 이름
                .imageKey(key).build();

        registerReviewImage(reviewImage, reviewId);

        return uploadImageUrl;
    }

    //리뷰 영수증 저장(DB)
    @Transactional
    protected Long registerReviewImage(ReviewImage reviewImage, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 섬네일 유무 확인
        if (review.getReviewImage() != null) {
            throw new IllegalStateException("이미 리뷰 영수증 인증이 있습니다.");
        }

        //FK 설정
        review.changeReviewImage(reviewImage);
        //영수증 인증 대기
        review.changeAuthenticationStatus(ReviewAuthentication.WAITING);

        reviewImageRepository.save(reviewImage);

        return reviewImage.getId();
    }

    //리뷰 영수증 사진 삭제하기(DB)
    @Transactional
    protected void deleteReviewImage(Long reviewImageId) {
        reviewRepository.deleteById(reviewImageId);
    }

}
