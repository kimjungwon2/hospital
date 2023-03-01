package site.hospital.common.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.HospitalThumbnail;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.hospital.user.repository.HospitalThumbnailRepository;

@Service
public class HospitalThumbnailImageService extends ImageManagementService{

    private final HospitalRepository hospitalRepository;
    private final HospitalThumbnailRepository hospitalThumbnailRepository;
    private final String dirName = "thumbnail";

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
    public String uploadImage(
            MultipartFile multipartFile
            , Long hospitalId
    ) throws IOException {
        File uploadImageFile = uploadLocal(multipartFile).orElseThrow(
                () -> new IllegalArgumentException(
                        "error: MultipartFile -> File convert fail"));

        return uploadThumbnail(uploadImageFile, hospitalId);
    }

    @Override
    public void deleteImage(Long imageId) {
        HospitalThumbnail hospitalThumbnail = hospitalThumbnailRepository.findById(imageId)
                .orElseThrow(() -> new IllegalStateException("등록되지 않은 섬네일입니다."));
        
        String imageKey = hospitalThumbnail.getImageKey();
        deleteS3Images(this.dirName, imageKey);
        deleteHospitalThumbnail(imageId, hospitalThumbnail);
    }

    @Transactional
    protected void deleteHospitalThumbnail(Long thumbnailId, HospitalThumbnail hospitalThumbnail) {

        Hospital hospital = hospitalRepository.findByHospitalThumbnail(hospitalThumbnail);
        hospital.deleteHospitalThumbnailId();

        hospitalThumbnailRepository.deleteById(thumbnailId);
    }

    @Transactional
    protected Long registerHospitalThumbnail(HospitalThumbnail hospitalThumbnail, Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        checkThumbnail(hospital);

        registerThumbnail(hospitalThumbnail, hospital);

        return hospitalThumbnail.getId();
    }

    private void registerThumbnail(HospitalThumbnail hospitalThumbnail, Hospital hospital) {
        hospital.changeHospitalThumbnail(hospitalThumbnail);
        hospitalThumbnailRepository.save(hospitalThumbnail);
    }

    private void checkThumbnail(Hospital hospital) {
        if (hospital.getHospitalThumbnail() != null) {
            throw new IllegalStateException("이미 섬네일이 있습니다.");
        }
    }

    private String uploadThumbnail(File uploadFile, Long hospitalId)
            throws NoSuchFileException, DirectoryNotEmptyException, IOException {

        String extension = confirmImageExtension(uploadFile);

        String imageName = createUUIDName(this.dirName, extension);
        String uploadImageUrl = putS3(uploadFile, imageName);

        removeLocalImage(uploadFile);

        String imageKey = getImageKey(this.dirName, imageName);

        registerThumbnailKey(uploadFile, hospitalId, imageKey);

        return uploadImageUrl;
    }

    private void registerThumbnailKey(File uploadFile, Long hospitalId, String imageKey) {
        HospitalThumbnail hospitalThumbnail =
                HospitalThumbnail
                .builder()
                .originalName(uploadFile.getName())
                .imageKey(imageKey)
                .build();

        registerHospitalThumbnail(hospitalThumbnail, hospitalId);
    }

}
