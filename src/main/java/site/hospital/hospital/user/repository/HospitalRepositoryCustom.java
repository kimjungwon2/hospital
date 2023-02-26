package site.hospital.hospital.user.repository;

import site.hospital.hospital.user.domain.Hospital;

public interface HospitalRepositoryCustom {

    Hospital viewHospital(Long hospitalId);

    Hospital findHospitalAdditionalInfoId(Long staffHosId);

    Hospital findByDoctorId(Long doctorId);

}
