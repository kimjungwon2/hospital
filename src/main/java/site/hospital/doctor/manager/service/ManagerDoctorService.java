package site.hospital.doctor.manager.service;

import javax.servlet.ServletRequest;
import site.hospital.doctor.manager.api.dto.DoctorCreateResponse;
import site.hospital.doctor.manager.api.dto.DoctorManagerCreateRequest;
import site.hospital.doctor.manager.api.dto.DoctorManagerModifyRequest;

public interface ManagerDoctorService {

    DoctorCreateResponse createDoctor(
            ServletRequest servletRequest,
            DoctorManagerCreateRequest request
    );

    void deleteDoctor(ServletRequest servletRequest, Long doctorId);

    Long modifyDoctor(
            Long doctorId,
            ServletRequest servletRequest,
            DoctorManagerModifyRequest request
    );

}
