package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.Hospital;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.domain.detailedHosInformation.HospitalAddress;
import site.hospital.domain.detailedHosInformation.HospitalLocation;
import site.hospital.repository.HospitalRepository;
import site.hospital.repository.StaffHosRepository;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final StaffHosRepository staffHosRepository;

    //병원 등록
    @Transactional
    public Long register(Hospital hospital){
        hospitalRepository.save(hospital);
        return hospital.getId();
    }

    @Transactional
    public Long registerDetailedHosInformation(Long hospitalId, int numberHealthcareProvider, int numberWard, int numberPatientRoom,
                                               HospitalAddress hospitalAddress, HospitalLocation hospitalLocation){
        Hospital hospital  = hospitalRepository.findById(hospitalId).orElse(null);

        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder().
                numberHealthcareProvider(numberHealthcareProvider)
                .numberWard(numberWard).numberPatientRoom(numberPatientRoom).
                        hospitalAddress(hospitalAddress).hospitalLocation(hospitalLocation)
        .build();

        hospital = Hospital.createDetailedHosInformation(detailedHosInformation);

        return hospital.getId();
    }

    //병원 추가정보 등록.
    @Transactional
    public Long registerStaffHosInformation(String photo, String introduction, String consultationHour, String abnormality){
        StaffHosInformation staffHosInformation = StaffHosInformation.builder()
                .photo(photo).introduction(introduction).consultationHour(consultationHour).abnormality(abnormality).build();
        staffHosRepository.save(staffHosInformation);

        Hospital hospital = Hospital.createStaffHosInformation(staffHosInformation);
        hospitalRepository.save(hospital);

        return hospital.getId();
    }
}
