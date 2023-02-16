package site.hospital.hospital.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.repository.searchQuery.HospitalSearchDto;
import site.hospital.hospital.user.repository.searchQuery.HospitalSearchRepository;
import site.hospital.hospital.user.repository.viewQuery.HospitalViewRepository;
import site.hospital.hospital.user.repository.viewQuery.ViewHospitalDTO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalSearchRepository hospitalSearchRepository;
    private final HospitalViewRepository hospitalViewRepository;

    //병원 검색
    public Page<HospitalSearchDto> searchHospital(String searchName, Pageable pageable) {
        return hospitalSearchRepository.searchHospital(searchName, pageable);
    }

    //병원 정보 상세 보기
    public ViewHospitalDTO viewHospital(Long hospitalId) {
        ViewHospitalDTO hospital = hospitalViewRepository.viewHospital(hospitalId);
        if (hospital == null) {
            throw new IllegalStateException("해당 병원은 존재하지 않습니다.");
        }
        return hospital;
    }
}
