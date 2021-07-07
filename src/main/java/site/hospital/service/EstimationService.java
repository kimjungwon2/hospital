package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Estimation;
import site.hospital.repository.EstimationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimationService {
    private final EstimationRepository estimationRepository;

    @Transactional
    public Long createEstimation(Estimation estimation){
        estimationRepository.save(estimation);

        return estimation.getId();
    }
}
