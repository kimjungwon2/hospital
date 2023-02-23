package site.hospital.common.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public abstract class ImageManagementService {

    protected final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    protected String bucket;

    abstract public String uploadImage(
            MultipartFile multipartFile,
            Long hospitalId)
            throws IOException;

    abstract public void deleteImage(Long imageId);

    protected String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    protected void removeLocalImage(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    protected Optional<File> uploadLocal(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    protected void deleteS3Images(String dirName, String imageKey) {
        String[] imageNames = getResizedImages(dirName, imageKey);

        deleteS3Images(imageNames);
    }

    protected String confirmImageExtension(File uploadFile) {
        String uploadImageName = uploadFile.getName();
        String extension = getExtension(uploadImageName);

        confirmImage(extension);

        return extension;
    }

    protected String getImageKey(String dirName, String imageName) {
        String key = imageName.replace(dirName + "/", "");
        return key;
    }

    protected String createUUIDName(String dirName, String extension) {
        return dirName + "/" + UUID.randomUUID() + "." + extension;
    }

    private void deleteS3Images(String[] imageNames) {
        for (int i = 0; i < 3; i++) {
            DeleteObjectRequest request = new DeleteObjectRequest(bucket, imageNames[i]);
            amazonS3Client.deleteObject(request);
        }
    }

    private String[] getResizedImages(String dirName, String imageKey) {
        String[] fileName = new String[3];

        fileName[0] = dirName + "/" + imageKey; // 기존 폴더
        fileName[1] = "w140" + "/" + imageKey; //w140 폴더
        fileName[2] = "w600" + "/" + imageKey; //w600 폴더

        return fileName;
    }

    private String getExtension(String uploadImageName) {
        String extension = uploadImageName.substring(uploadImageName.lastIndexOf(".") + 1);
        extension = extension.toLowerCase();
        return extension;
    }

    private void confirmImage(String extension) {
        if (!extension.equals("bmp") && !extension.equals("rle") && !extension.equals("dib")
                && !extension.equals("jpeg") && !extension.equals("jpg")
                && !extension.equals("png") && !extension.equals("gif")
                && !extension.equals("jfif") && !extension.equals("tif")
                && !extension.equals("tiff") && !extension
                .equals("raw")) {
            throw new IllegalStateException("이미지 확장자가 아닙니다.");
        }
    }

}