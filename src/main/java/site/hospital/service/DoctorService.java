package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Doctor;
import site.hospital.domain.StaffHosInformation;
import site.hospital.repository.DoctorRepository;
import site.hospital.repository.StaffHosRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final StaffHosRepository staffHosRepository;

    @Transactional
    public Long registerDoctor(Long staffHosInfoId, Doctor doctor){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosInfoId).orElse(null);
        doctor.changeStaffHosInformation(staffHosInformation);
        doctorRepository.save(doctor);

        return doctor.getId();
    }

    //의사 수정
    @Transactional
    public Long modifyDoctor(Long doctorId, ModifyDoctorRequest request){
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        doctor.modifyDoctor(request.getName(), request.getHistory(), request.getPhoto());

        return doctor.getId();
    }

    //의사 삭제
    @Transactional
    public void deleteDoctor(Long id){
        doctorRepository.deleteById(id);
    }
}
