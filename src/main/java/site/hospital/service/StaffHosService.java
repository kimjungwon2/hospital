package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.Doctor;
import site.hospital.dto.ModifyStaffHospitalRequest;
import site.hospital.repository.StaffHosRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffHosService {
    private final StaffHosRepository staffHosRepository;

    //병원 추가 정보 수정
    @Transactional
    public Long modifyStaffHosInformation(Long staffId, ModifyStaffHospitalRequest request){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffId).orElse(null);
        staffHosInformation.modifyStaffHosInformation(request.getPhoto(),request.getIntroduction(),
                request.getConsultationHour(),request.getAbnormality());

        return staffHosInformation.getId();
    }

    //병원 추가 정보 삭제
   @Transactional
    public void deleteStaffHosInformation(Long id){
        staffHosRepository.deleteById(id);
    }

    //의사 등록
    @Transactional
    public Long doctorRegister(String name, String history, String photo){
        Doctor doctor = Doctor.builder().name(name).history(history).photo(photo).build();

        StaffHosInformation staffHosInformation = StaffHosInformation.createDoctor(doctor);
        staffHosRepository.save(staffHosInformation);

        return staffHosInformation.getId();
    }

}
