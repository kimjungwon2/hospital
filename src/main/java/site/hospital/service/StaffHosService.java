package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.Doctor;
import site.hospital.repository.StaffHosRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffHosService {
    private final StaffHosRepository staffHosRepository;

    //의사 등록
    @Transactional
    public Long doctorRegister(String name, String history, String photo){
        Doctor doctor = Doctor.builder().name(name).history(history).photo(photo).build();

        StaffHosInformation staffHosInformation = StaffHosInformation.createDoctor(doctor);
        staffHosRepository.save(staffHosInformation);

        return staffHosInformation.getId();
    }

}
