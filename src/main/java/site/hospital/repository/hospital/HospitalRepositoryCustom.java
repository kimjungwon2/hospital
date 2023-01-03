package site.hospital.repository.hospital;

import site.hospital.domain.hospital.Hospital;

public interface HospitalRepositoryCustom {

    Hospital viewHospital(Long hospitalId);

    Hospital findByStaffHosId(Long staffHosId);

    Hospital findByDoctorId(Long doctorId);
}
