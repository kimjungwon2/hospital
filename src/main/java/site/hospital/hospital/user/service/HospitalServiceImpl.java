package site.hospital.hospital.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.repository.search.HospitalSearchRepository;
import site.hospital.hospital.user.repository.search.HospitalSearchSelectQuery;
import site.hospital.hospital.user.repository.view.HospitalViewRepository;
import site.hospital.hospital.user.repository.view.HospitalViewSelectQuery;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalSearchRepository hospitalSearchRepository;
    private final HospitalViewRepository hospitalViewRepository;

    @Override
    public Page<HospitalSearchSelectQuery> searchHospital(String searchName, Pageable pageable) {
        return hospitalSearchRepository.searchHospitals(searchName, pageable);
    }

    @Override
    public HospitalViewSelectQuery viewHospital(Long hospitalId) {
        HospitalViewSelectQuery hospital = hospitalViewRepository.viewHospital(hospitalId);

        if (hospital == null) {
            throw new IllegalStateException("해당 병원은 존재하지 않습니다.");
        }

        return hospital;
    }
}
