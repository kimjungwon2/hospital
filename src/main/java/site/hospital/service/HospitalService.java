package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.DetailedHosInformation;
import site.hospital.domain.Hospital;
import site.hospital.repository.HospitalRepository;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    //병원 등록
    @Transactional
    public Long register(Hospital hospital){
        hospitalRepository.save(hospital);
        return hospital.getId();
    }

    //병원 상세정보 등록
    @Transactional
    public Long registerDetailedHosInformation(String photo, String introduction, String consultationHour, String abnormality){
        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                .photo(photo).introduction(introduction).consultationHour(consultationHour).abnormality(abnormality).build();
        Hospital hospital = Hospital.createDetailedHosInformation(detailedHosInformation);

        hospitalRepository.save(hospital);
        return hospital.getId();
    }
}
