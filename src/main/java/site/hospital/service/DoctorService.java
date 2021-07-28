package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Doctor;
import site.hospital.domain.StaffHosInformation;
import site.hospital.dto.ModifyDoctorRequest;
import site.hospital.dto.doctor.CreateDoctorRequest;
import site.hospital.repository.DoctorRepository;
import site.hospital.repository.StaffHosRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final StaffHosRepository staffHosRepository;

    //doctor 생성
    @Transactional
    public Long createDoctor(CreateDoctorRequest request){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(request.getStaffHosId())
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        Doctor doctor =Doctor.builder().staffHosInformation(staffHosInformation).history(request.getHistory())
                .name(request.getName()).build();

        doctorRepository.save(doctor);

        return doctor.getId();
    }

    //doctor 수정
    @Transactional
    public Long registerDoctor(Long staffHosInfoId, Doctor doctor){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosInfoId).orElse(null);
        doctorRepository.save(doctor);

        return doctor.getId();
    }

    //의사 수정
    @Transactional
    public Long modifyDoctor(Long doctorId, Doctor modifyDoctor){
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        doctor.modifyDoctor(modifyDoctor);

        return doctor.getId();
    }

    //의사 삭제
    @Transactional
    public void deleteDoctor(Long id){
        doctorRepository.deleteById(id);
    }
}
