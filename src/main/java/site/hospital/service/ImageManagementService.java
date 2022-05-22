package site.hospital.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.domain.HospitalThumbnail;
import site.hospital.domain.ReviewImage;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.review.Review;
import site.hospital.domain.review.ReviewAuthentication;
import site.hospital.repository.HospitalThumbnailRepository;
import site.hospital.repository.ReviewImageRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.review.ReviewRepository;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageManagementService {

    private final AmazonS3Client amazonS3Client;
    private final HospitalRepository hospitalRepository;
    private final HospitalThumbnailRepository hospitalThumbnailRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;

    // S3 버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    //섬네일 파일 업로드
    public String thumbnailUpload(MultipartFile multipartFile, String dirName, Long hospitalId) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return thumbnailUpload(uploadFile, dirName, hospitalId);
    }

    //리뷰 영수증 이미지 업로드
    public String reviewReceiptUpload(MultipartFile multipartFile, String dirName, Long reviewId) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return reviewImageUpload(uploadFile, dirName, reviewId);
    }

    // preSigned 받아오기
    public String preSignedUpload(MultipartFile multipartFile, String dirName, Long hospitalId) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return getPreSignedURL(uploadFile, dirName, hospitalId);
    }

    // S3로 섬네일 파일 업로드하기
    private String thumbnailUpload(File uploadFile, String dirName, Long hospitalId) {

        //확장자
        String uploadName = uploadFile.getName();
        String extension = uploadName.substring(uploadName.lastIndexOf(".")+1);

        //이미지 파일 확장자가 아닌 경우 exception 발생.
        if(!extension.equals("bmp")&&!extension.equals("rle")&&!extension.equals("dib")
                &&!extension.equals("jpeg")
                &&!extension.equals("jpg")&&!extension.equals("png") &&!extension.equals("gif")
                &&!extension.equals("tif")&&!extension.equals("tiff")&&!extension.equals("raw")){
            throw new IllegalStateException("이미지 확장자가 아닙니다.");
        }

        String fileName = dirName + "/" + UUID.randomUUID() + "." + extension; // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);

        String key = fileName.replace(dirName+"/",""); // 키 값 저장.

        //DB에 정보 저장.
        HospitalThumbnail hospitalThumbnail = HospitalThumbnail.builder()
                .originalName(uploadFile.getName()) // 파일 원본 이름
                .imageKey(key).build();

        registerHospitalThumbnail(hospitalThumbnail,hospitalId);

        return uploadImageUrl;
    }

    // S3로 리뷰 영수증 업로드
    private String reviewImageUpload(File uploadFile, String dirName, Long reviewId) {

        //확장자 찾기
        String uploadName = uploadFile.getName();
        String extension = uploadName.substring(uploadName.lastIndexOf(".")+1);

        //이미지 파일 확장자가 아닌 경우 exception 발생.
        if(!extension.equals("bmp")&&!extension.equals("rle")&&!extension.equals("dib")
                &&!extension.equals("jpeg")
                &&!extension.equals("jpg")&&!extension.equals("png") &&!extension.equals("gif")
                &&!extension.equals("tif")&&!extension.equals("tiff")&&!extension.equals("raw")){
            throw new IllegalStateException("이미지 확장자가 아닙니다.");
        }

        String fileName = dirName + "/" + UUID.randomUUID()+ "." + extension; // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);

        String key = fileName.replace(dirName+"/",""); // 키 값 저장.

        //DB에 정보 저장.
        ReviewImage reviewImage = ReviewImage.builder()
                .originalName(uploadFile.getName()) // 파일 원본 이름
                .imageKey(key).build();

        registerReviewImage(reviewImage, reviewId);

        return uploadImageUrl;
    }



    //Presigned URL 획득
    private String getPreSignedURL(File uploadFile, String dirName, Long hospitalId) {
        String preSignedURL = "";

        //MimeType 찾기.
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String firstType = fileTypeMap.getContentType(uploadFile.getName());
        String mimeType = firstType.replace("image/","."); // mimeType(확장자 명)

        // S3에 저장될 파일 이름
        String fileName = dirName + "/" + UUID.randomUUID()+mimeType;

        //유효기간 설정
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 5; // 5분 설정.
        expiration.setTime(expTimeMillis);

        log.info(expiration.toString());

        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucket, fileName)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);

            generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL,
                    CannedAccessControlList.PublicRead.toString());

            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

            preSignedURL = url.toString();

            log.info("Pre-Signed URL : " + url.toString());
            removeNewFile(uploadFile); //로컬에 업로드 된 파일 삭제

        } catch (Exception e) {
            e.printStackTrace();
        }
        return preSignedURL;
    }

    //섬네일 삭제하기
    public void deleteThumbnail(Long thumbnailId, String dirName){
        HospitalThumbnail hospitalThumbnail = hospitalThumbnailRepository.findById(thumbnailId)
                .orElseThrow(()->new IllegalStateException("등록되지 않은 섬네일입니다."));

        //이미지 키 찾기
        String imageKey = hospitalThumbnail.getImageKey();
        String[] fileName = new String[3];

        //3개의 파일 삭제
        fileName[0] = dirName + "/" +imageKey; // 기존 폴더
        fileName[1] = "w140" + "/" +imageKey; //w140 폴더
        fileName[2] = "w600" + "/" +imageKey; //w600 폴더

        //S3 이미지 연속해서 삭제.
        for(int i = 0; i<3; i++){
            DeleteObjectRequest request = new DeleteObjectRequest(bucket,fileName[i]);
            amazonS3Client.deleteObject(request);
        }

        //이미지 DB 삭제
        deleteHospitalThumbnail(thumbnailId,hospitalThumbnail);
    }


    //병원 섬네일 등록하기(DB)
    @Transactional
    private Long registerHospitalThumbnail(HospitalThumbnail hospitalThumbnail, Long hospitalId){
        Hospital hospital = hospitalRepository.findById(hospitalId).
                orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 섬네일 유무 확인
        if(hospital.getHospitalThumbnail() !=null) throw new IllegalStateException("이미 섬네일이 있습니다.");

        hospital.changeHospitalThumbnail(hospitalThumbnail);
        hospitalThumbnailRepository.save(hospitalThumbnail);

        return hospitalThumbnail.getId();
    }

    //병원 섬네일 삭제하기(DB)
    @Transactional
    private void deleteHospitalThumbnail(Long thumbnailId,HospitalThumbnail hospitalThumbnail){

        Hospital hospital = hospitalRepository.findByHospitalThumbnail(hospitalThumbnail);
        hospital.deleteHospitalThumbnailId();

        //섬네일 DB 삭제
        hospitalThumbnailRepository.deleteById(thumbnailId);
    }

    //리뷰 영수증 저장(DB)
    @Transactional
    private Long registerReviewImage(ReviewImage reviewImage, Long reviewId){
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 섬네일 유무 확인
        if(review.getReviewImage() !=null) throw new IllegalStateException("이미 리뷰 영수증 인증이 있습니다.");

        //FK 설정
        review.changeReviewImage(reviewImage);
        //영수증 인증 대기
        review.changeAuthenticationStatus(ReviewAuthentication.WAITING);

        reviewImageRepository.save(reviewImage);

        return reviewImage.getId();
    }

    //리뷰 영수증 사진 삭제하기(DB)
    @Transactional
    private void deleteReviewImage(Long reviewImageId){
        reviewRepository.deleteById(reviewImageId);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}