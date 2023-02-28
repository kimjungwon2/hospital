package site.hospital.doctor.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.doctor.admin.api.dto.DoctorAdminModifyRequest;
import site.hospital.doctor.manager.api.dto.DoctorCreateResponse;
import site.hospital.doctor.manager.domain.Doctor;
import site.hospital.doctor.manager.repository.DoctorRepository;
import site.hospital.doctor.admin.api.dto.DoctorAdminCreateRequest;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;


public interface AdminDoctorService {

    Long modifyDoctor(Long doctorId, DoctorAdminModifyRequest request);

    void deleteDoctor(Long doctorId);

    DoctorCreateResponse createDoctor(DoctorAdminCreateRequest request);

}
