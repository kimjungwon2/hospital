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
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.HospitalThumbnail;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.hospital.user.repository.HospitalThumbnailRepository;

@Slf4j
@Service
public class HospitalThumbnailImageService extends ImageManagementService{

    private final HospitalRepository hospitalRepository;
    private final HospitalThumbnailRepository hospitalThumbnailRepository;

    public HospitalThumbnailImageService(
            AmazonS3Client amazonS3Client,
            HospitalRepository hospitalRepository,
            HospitalThumbnailRepository hospitalThumbnailRepository
    ) {
        super(amazonS3Client);
        this.hospitalRepository = hospitalRepository;
        this.hospitalThumbnailRepository = hospitalThumbnailRepository;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile, String dirName, Long hospitalId)
            throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(
                () -> new IllegalArgumentException(
                        "error: MultipartFile -> File convert fail"));

        return thumbnailUpload(uploadFile, dirName, hospitalId);
    }

    @Override
    public void deleteImage(Long imageId, String dirName) {
        HospitalThumbnail hospitalThumbnail = hospitalThumbnailRepository.findById(imageId)
                .orElseThrow(() -> new IllegalStateException("등록되지 않은 섬네일입니다."));

        //이미지 키 찾기
        String imageKey = hospitalThumbnail.getImageKey();
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
        deleteHospitalThumbnail(imageId, hospitalThumbnail);
    }



    // S3로 섬네일 파일 업로드하기
    private String thumbnailUpload(File uploadFile, String dirName, Long hospitalId) {

        //확장자
        String uploadName = uploadFile.getName();
        String extension = uploadName.substring(uploadName.lastIndexOf(".") + 1);
        extension = extension.toLowerCase();

        //이미지 파일 확장자가 아닌 경우 exception 발생.
        if (!extension.equals("bmp") && !extension.equals("rle") && !extension.equals("dib")
                && !extension.equals("jpeg") && !extension.equals("jpg")
                && !extension.equals("png") && !extension.equals("gif")
                && !extension.equals("jfif") && !extension.equals("tif")
                && !extension.equals("tiff") && !extension
                .equals("raw")) {
            throw new IllegalStateException("이미지 확장자가 아닙니다.");
        }

        String fileName = dirName + "/" + UUID.randomUUID() + "." + extension; // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);

        String key = fileName.replace(dirName + "/", ""); // 키 값 저장.

        //DB에 정보 저장.
        HospitalThumbnail hospitalThumbnail = HospitalThumbnail.builder()
                .originalName(uploadFile.getName()) // 파일 원본 이름
                .imageKey(key).build();

        registerHospitalThumbnail(hospitalThumbnail, hospitalId);

        return uploadImageUrl;
    }

    //병원 섬네일 삭제하기(DB)
    @Transactional
    protected void deleteHospitalThumbnail(Long thumbnailId, HospitalThumbnail hospitalThumbnail) {

        Hospital hospital = hospitalRepository.findByHospitalThumbnail(hospitalThumbnail);
        hospital.deleteHospitalThumbnailId();

        //섬네일 DB 삭제
        hospitalThumbnailRepository.deleteById(thumbnailId);
    }

    //병원 섬네일 등록하기(DB)
    @Transactional
    protected Long registerHospitalThumbnail(HospitalThumbnail hospitalThumbnail, Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 섬네일 유무 확인
        if (hospital.getHospitalThumbnail() != null) {
            throw new IllegalStateException("이미 섬네일이 있습니다.");
        }

        hospital.changeHospitalThumbnail(hospitalThumbnail);
        hospitalThumbnailRepository.save(hospitalThumbnail);

        return hospitalThumbnail.getId();
    }

}
