package site.hospital.hospital.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.repository.searchQuery.HospitalSearchDto;

public interface HospitalRepositoryCustom {

    Hospital viewHospital(Long hospitalId);

    Hospital findByStaffHosId(Long staffHosId);

    Hospital findByDoctorId(Long doctorId);

    Page<Hospital> searchHospitalVer2(String searchName, Pageable pageable);

    Page<HospitalSearchDto> searchHospitalVer1(String searchName, Pageable pageable);
}
