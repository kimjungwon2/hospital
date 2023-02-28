package site.hospital.hospital.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.hospital.user.repository.search.HospitalSearchSelectQuery;
import site.hospital.hospital.user.repository.view.HospitalViewSelectQuery;

public interface HospitalService {

    Page<HospitalSearchSelectQuery> searchHospital(String searchName, Pageable pageable);

    HospitalViewSelectQuery viewHospital(Long hospitalId);
}
