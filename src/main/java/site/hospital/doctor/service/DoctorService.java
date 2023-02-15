package site.hospital.doctor.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.doctor.api.dto.DoctorAdminModifyRequest;
import site.hospital.doctor.api.dto.DoctorCreateResponse;
import site.hospital.doctor.api.dto.DoctorStaffModifyRequest;
import site.hospital.doctor.domain.Doctor;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.doctor.repository.dto.CreateDoctorRequest;
import site.hospital.doctor.repository.dto.StaffCreateDoctorRequest;
import site.hospital.doctor.repository.DoctorRepository;
import site.hospital.hospital.user.repository.StaffHosRepository;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.common.service.JwtStaffAccessService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final StaffHosRepository staffHosRepository;
    private final HospitalRepository hospitalRepository;
    private final JwtStaffAccessService jwtStaffAccessService;

    //병원 관계자 doctor 생성
    @Transactional
    public DoctorCreateResponse staffCreateDoctor(ServletRequest servletRequest, StaffCreateDoctorRequest request) {
        //해당 id 값을 가지고 있는 병원 검색
        Hospital hospital = hospitalRepository.findByStaffHosId(request.getStaffHosId());

        if (hospital == null) {
            throw new IllegalStateException("병원이 존재하지 않습니다.");
        }

        jwtStaffAccessService
                .staffAccessFunction(servletRequest, request.getMemberId(), hospital.getId());

        StaffHosInformation staffHosInformation = staffHosRepository
                .findById(request.getStaffHosId())
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        Doctor doctor = Doctor.builder().staffHosInformation(staffHosInformation)
                .history(request.getHistory())
                .name(request.getName()).build();

        doctorRepository.save(doctor);

        return DoctorCreateResponse.from(doctor.getId());
    }

    //병원 관계자 의사 수정
    @Transactional
    public Long staffModifyDoctor(
            ServletRequest servletRequest,
            Long memberId,
            Long doctorId,
            DoctorStaffModifyRequest request
    ) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 의사가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findByDoctorId(doctorId);

        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospital.getId());

        Doctor modifyDoctor = Doctor.builder()
                .history(request.getHistory())
                .name(request.getName()).build();

        doctor.modifyDoctor(modifyDoctor);

        return doctor.getId();
    }

    //병원 관계자 의사 삭제
    @Transactional
    public void staffDeleteDoctor(ServletRequest servletRequest, Long memberId, Long doctorId) {
        Hospital hospital = hospitalRepository.findByDoctorId(doctorId);

        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospital.getId());

        doctorRepository.deleteById(doctorId);
    }


    //doctor 생성
    @Transactional
    public DoctorCreateResponse createDoctor(CreateDoctorRequest request) {
        StaffHosInformation staffHosInformation = staffHosRepository
                .findById(request.getStaffHosId())
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        Doctor doctor = Doctor.builder().staffHosInformation(staffHosInformation)
                .history(request.getHistory())
                .name(request.getName()).build();

        doctorRepository.save(doctor);

        return DoctorCreateResponse.from(doctor.getId());
    }

    //의사 수정
    @Transactional
    public Long modifyDoctor(Long doctorId, DoctorAdminModifyRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 의사가 존재하지 않습니다."));

        Doctor modifyDoctor = Doctor.builder()
                .history(request.getHistory())
                .name(request.getName()).build();

        doctor.modifyDoctor(modifyDoctor);

        return doctor.getId();
    }

    //의사 삭제
    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
