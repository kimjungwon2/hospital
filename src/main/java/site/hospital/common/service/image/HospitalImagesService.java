package site.hospital.common.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.HospitalImage;
import site.hospital.hospital.user.repository.HospitalImageRepository;
import site.hospital.hospital.user.repository.HospitalRepository;

@Service
public class HospitalImagesService extends ImageManagementService{

    private final HospitalRepository hospitalRepository;
    private final HospitalImageRepository hospitalImageRepository;
    private final String dirName = "hospitalImage";


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
            Long hospitalId
    ) {
        return null;
    }

    @Override
    public void deleteImage(Long imageId) {
        HospitalImage hospitalImage = hospitalImageRepository
                .findById(imageId)
                .orElseThrow(
                        () -> new IllegalStateException("등록되지 않은 병원 이미지입니다."));

        String imageKey = hospitalImage.getImageKey();
        deleteS3Images(this.dirName, imageKey);
        deleteHospitalImage(imageId);
    }

    public List<String> uploadImage(
            List<MultipartFile> multiparFile,
            Long hospitalId
    ) throws IOException {
        List<File> uploadFile = uploadLocalMultipleFile(multiparFile);

        return uploadHospitalImages(uploadFile, hospitalId);
    }

    @Transactional
    protected Long registerHospitalImage(String originalName, String key, Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        HospitalImage hospitalImage = saveHospitalImage(originalName, key, hospital);

        return hospitalImage.getId();
    }

    @Transactional
    protected void deleteHospitalImage(Long hospitalImageId) {

        HospitalImage hospitalImage = hospitalImageRepository.
                findById(hospitalImageId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 이미지가 존재하지 않습니다."));

        hospitalImageRepository.deleteById(hospitalImage.getId());
    }

    private HospitalImage saveHospitalImage(String originalName, String key, Hospital hospital) {
        HospitalImage hospitalImage = HospitalImage
                .builder()
                .originalName(originalName)
                .imageKey(key)
                .hospital(hospital)
                .build();

        hospitalImageRepository.save(hospitalImage);
        return hospitalImage;
    }

    private List<String> uploadHospitalImages(
            List<File> uploadFiles,
            Long hospitalId
    ) {

        List<String> uploadImageUrls = new ArrayList<>();

        for (File uploadFile : uploadFiles) {

            String imageExtension = confirmImageExtension(uploadFile);

            String imageName = createUUIDName(this.dirName, imageExtension);
            String uploadImageUrl = putS3(uploadFile, imageName);
            removeLocalImage(uploadFile);

            uploadImageUrls.add(uploadImageUrl);

            String imageKey = getImageKey(this.dirName, imageName);

            registerHospitalImage(uploadFile.getName(), imageKey, hospitalId);
        }

        return uploadImageUrls;
    }

    private List<File> uploadLocalMultipleFile(List<MultipartFile> files) throws IOException {
        List<File> convertFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            File convertFile = new File(
                    System.getProperty("user.dir") + "/" + file.getOriginalFilename());
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(
                        convertFile)) {
                    fos.write(file.getBytes());
                }
                convertFiles.add(convertFile);
            }
        }
        return convertFiles;
    }

}
