package site.hospital.doctor.admin.service;

import site.hospital.doctor.admin.api.dto.DoctorAdminCreateRequest;
import site.hospital.doctor.admin.api.dto.DoctorAdminModifyRequest;
import site.hospital.doctor.manager.api.dto.DoctorCreateResponse;


public interface AdminDoctorService {

    Long modifyDoctor(Long doctorId, DoctorAdminModifyRequest request);

    void deleteDoctor(Long doctorId);

    DoctorCreateResponse createDoctor(DoctorAdminCreateRequest request);

}
