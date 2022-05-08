package site.hospital.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.service.S3Uploader;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Slf4j
public class TestController {

    private final S3Uploader s3Uploader;

    @PostMapping("/images")
    public String upload(@RequestParam(value = "images", required = false) MultipartFile multipartFile) throws IOException {
        s3Uploader.upload(multipartFile, "raw");
        return "test";
    }
}
