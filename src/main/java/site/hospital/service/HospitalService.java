package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Hospital;
import site.hospital.repository.HospitalRepository;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    //병원 등록
    @Transactional
    public Long register(Hospital hospital){
        hospitalRepository.save(hospital);
        return hospital.getId();
    }

}
