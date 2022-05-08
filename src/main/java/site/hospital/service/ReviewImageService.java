package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.repository.ReviewImageRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewImageService{
    private final ReviewImageRepository reviewImageRepository;
}
