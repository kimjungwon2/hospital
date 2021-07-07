package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.DetailedHosInformation;
import site.hospital.domain.Doctor;
import site.hospital.repository.DetailedHosRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DetailedHosService {
    private final DetailedHosRepository detailedHosRepository;

    //의사 등록
    @Transactional
    public Long doctorRegister(String name, String history, String photo){
        Doctor doctor = Doctor.builder().name(name).history(history).photo(photo).build();

        DetailedHosInformation detailedHosInformation = DetailedHosInformation.createDoctor(doctor);
        detailedHosRepository.save(detailedHosInformation);

        return detailedHosInformation.getId();
    }

}
