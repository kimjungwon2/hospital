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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminDoctorService {

    private final DoctorRepository doctorRepository;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;

    @Transactional
    public Long modifyDoctor(Long doctorId, DoctorAdminModifyRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalStateException("의사가 존재하지 않습니다."));

        Doctor modifiedDoctor = Doctor
                .builder()
                .history(request.getHistory())
                .name(request.getName())
                .build();

        doctor.modifyDoctor(modifiedDoctor);

        return doctor.getId();
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    @Transactional
    public DoctorCreateResponse createDoctor(DoctorAdminCreateRequest request) {
        StaffHosInformation hospitalAdditionalInfo = hospitalAdditionalInfoRepository
                .findById(request.getHospitalAdditionalInfoId())
                .orElseThrow(
                        () -> new IllegalStateException("HospitalAdditionalInfo does not exist."));

        Doctor doctor = Doctor
                .builder()
                .staffHosInformation(hospitalAdditionalInfo)
                .history(request.getHistory())
                .name(request.getName())
                .build();

        doctorRepository.save(doctor);

        return DoctorCreateResponse.from(doctor.getId());
    }


}
