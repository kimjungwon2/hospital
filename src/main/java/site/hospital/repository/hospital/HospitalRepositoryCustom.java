package site.hospital.repository.hospital;

import site.hospital.domain.Hospital;

public interface HospitalRepositoryCustom {
    Hospital adminViewHospital(Long hospitalId);
    Hospital findByStaffHosId(Long staffHosId);
}
