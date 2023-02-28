package site.hospital.doctor.manager.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtService;
import site.hospital.doctor.manager.api.dto.DoctorCreateResponse;
import site.hospital.doctor.manager.api.dto.DoctorManagerCreateRequest;
import site.hospital.doctor.manager.api.dto.DoctorManagerModifyRequest;
import site.hospital.doctor.manager.domain.Doctor;
import site.hospital.doctor.manager.repository.DoctorRepository;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;
import site.hospital.hospital.user.repository.HospitalRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerDoctorServiceImpl implements ManagerDoctorService {

    private final DoctorRepository doctorRepository;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;
    private final HospitalRepository hospitalRepository;
    private final ManagerJwtService managerJwtService;

    @Transactional
    @Override
    public DoctorCreateResponse createDoctor(
            ServletRequest servletRequest,
            DoctorManagerCreateRequest request
    ) {
        Hospital hospital = checkEmptyHospitalAdditionalInfo(request);

        managerJwtService
                .accessManager(servletRequest, request.getMemberId(), hospital.getId());

        StaffHosInformation hospitalAdditionalInfo = hospitalAdditionalInfoRepository
                .findById(request.getHospitalAdditionalInfoId())
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        Doctor doctor = saveDoctor(request, hospitalAdditionalInfo);

        return DoctorCreateResponse.from(doctor.getId());
    }

    @Transactional
    @Override
    public void deleteDoctor(ServletRequest servletRequest, Long memberId, Long doctorId) {
        Hospital hospital = hospitalRepository.findByDoctorId(doctorId);
        managerJwtService.accessManager(servletRequest, memberId, hospital.getId());
        doctorRepository.deleteById(doctorId);
    }

    @Transactional
    @Override
    public Long modifyDoctor(
            Long doctorId,
            ServletRequest servletRequest,
            DoctorManagerModifyRequest request
    ) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalStateException("의사가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findByDoctorId(doctorId);

        managerJwtService.accessManager(servletRequest, request.getMemberId(), hospital.getId());

        Doctor modifiedDoctor = Doctor
                .builder()
                .history(request.getHistory())
                .name(request.getName())
                .build();

        doctor.modifyDoctor(modifiedDoctor);

        return doctor.getId();
    }

    private Doctor saveDoctor(DoctorManagerCreateRequest request,
            StaffHosInformation hospitalAdditionalInfo) {
        Doctor doctor = Doctor.builder()
                .staffHosInformation(hospitalAdditionalInfo)
                .history(request.getHistory())
                .name(request.getName())
                .build();

        doctorRepository.save(doctor);
        return doctor;
    }

    private Hospital checkEmptyHospitalAdditionalInfo(DoctorManagerCreateRequest request) {
        Hospital hospital = hospitalRepository.findHospitalByHosAdditionalInfoId(request.getHospitalAdditionalInfoId());

        if (hospital == null) {
            throw new IllegalStateException("병원이 존재하지 않습니다.");
        }
        return hospital;
    }

}
