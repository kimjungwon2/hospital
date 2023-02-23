package site.hospital.common.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.HospitalImage;
import site.hospital.hospital.user.domain.HospitalThumbnail;
import site.hospital.hospital.user.repository.HospitalImageRepository;
import site.hospital.hospital.user.repository.HospitalRepository;

@Slf4j
@Service
public class HospitalImagesService extends ImageManagementService{

    private final HospitalRepository hospitalRepository;
    private final HospitalImageRepository hospitalImageRepository;


    public HospitalImagesService(
            AmazonS3Client amazonS3Client,
            HospitalRepository hospitalRepository,
            HospitalImageRepository hospitalImageRepository
    ) {
        super(amazonS3Client);
        this.hospitalRepository = hospitalRepository;
        this.hospitalImageRepository = hospitalImageRepository;
    }

    @Override
    public String uploadImage(
            MultipartFile multipartFile,
            String dirName,
            Long hospitalId
    ) throws IOException {
        return null;
    }

    public List<String> uploadImages(
            List<MultipartFile> multiparFile,
            String dirName,
            Long hospitalId
    ) throws IOException {
        List<File> uploadFile = convertMultiple(multiparFile);

        return hospitalsImageUpload(uploadFile, dirName, hospitalId);
    }

    @Override
    public void deleteImage(Long imageId, String dirName) {
        HospitalImage hospitalImage = hospitalImageRepository
                .findById(imageId)
                .orElseThrow(
                        () -> new IllegalStateException("등록되지 않은 병원 이미지입니다."));

        //이미지 키 찾기
        String imageKey = hospitalImage.getImageKey();
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
        deleteHospitalImage(imageId);
    }


    //S3 병원 이미지 업로드(다수)
    private List<String> hospitalsImageUpload(List<File> uploadFiles, String dirName,
            Long hospitalId) {

        List<String> uploadImageUrls = new ArrayList<>();

        for (File uploadFile : uploadFiles) {
            //확장자 찾기
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

            uploadImageUrls.add(uploadImageUrl);

            String key = fileName.replace(dirName + "/", ""); // 키 값 저장.

            registerHospitalImage(uploadFile.getName(), key, hospitalId);
        }
        return uploadImageUrls;
    }


    //병원 이미지 등록하기(DB)
    @Transactional
    protected Long registerHospitalImage(String originalName, String key, Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //DB에 정보 저장.
        HospitalImage hospitalImage = HospitalImage.builder()
                .originalName(originalName) // 파일 원본 이름
                .imageKey(key).hospital(hospital).build();

        hospitalImageRepository.save(hospitalImage);

        return hospitalImage.getId();
    }

    //병원 이미지 삭제하기(DB)
    @Transactional
    protected void deleteHospitalImage(Long hospitalImageId) {

        HospitalImage hospitalImage = hospitalImageRepository.
                findById(hospitalImageId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 이미지가 존재하지 않습니다."));

        //병원 이미지 DB 삭제
        hospitalImageRepository.deleteById(hospitalImageId);
    }

    private List<File> convertMultiple(List<MultipartFile> files) throws IOException {
        List<File> convertFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            File convertFile = new File(
                    System.getProperty("user.dir") + "/" + file.getOriginalFilename());
            if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
                try (FileOutputStream fos = new FileOutputStream(
                        convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                    fos.write(file.getBytes());
                }
                convertFiles.add(convertFile);
            }
        }
        return convertFiles;
    }

}
