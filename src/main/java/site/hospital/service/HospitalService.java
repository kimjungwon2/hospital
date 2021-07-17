package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.Hospital;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.repository.DetailedHosRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.StaffHosRepository;
import site.hospital.repository.hospital.query.HospitalSearchCondition;
import site.hospital.repository.hospital.query.HospitalSearchDto;
import site.hospital.repository.hospital.query.HospitalSearchRepository;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final StaffHosRepository staffHosRepository;
    private final HospitalSearchRepository hospitalSearchRepository;

    //병원 + 상세 정보등록
    @Transactional
    public Long register(Hospital hospital,DetailedHosInformation detailedHosInformation){

        //병원 테이블 상세정보 유무 확인
        if(hospital.getDetailedHosInformation() !=null) throw new IllegalStateException("이미 상세 정보가 있습니다.");

        hospital.changeDetailedHosInformation(detailedHosInformation);
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    //병원만 등록.
    @Transactional
    public Long registerHospital(Hospital hospital){
        hospitalRepository.save(hospital);

        return hospital.getId();
    }


    //병원 추가정보 등록.
    @Transactional
    public Long registerStaffHosInformation(Long hospitalId, String photo, String introduction,
                                            String consultationHour, String abnormality){
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        //병원 테이블 추가정보 유무 확인
        if(hospital.getStaffHosInformation() !=null) throw new IllegalStateException("이미 추가 정보가 있습니다.");

        StaffHosInformation staffHosInformation = StaffHosInformation.builder()
                .photo(photo).introduction(introduction).consultationHour(consultationHour).abnormality(abnormality).build();

        staffHosRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);
        hospitalRepository.save(hospital);

        return hospital.getId();
    }



    //병원 + 상세 정보 수정
    @Transactional
    public Long modifyAllHosInformation(Long hospitalId, Long detailedHosId,
                                        ModifyHospitalRequest request){
        //병원 정보만 수정.
        Hospital selectHospital = hospitalRepository.findById(hospitalId).orElse(null);

        if(detailedHosId == null) {

            selectHospital.updateHospital(request.getLicensingDate(), request.getHospitalName(), request.getPhoneNumber(),
                    request.getDistinguishedName(), request.getMedicalSubject(),
                    request.getMedicalSubjectInformation(), request.getBusinessCondition(),
                    request.getCityName());
        }
        //병원 + 상세 정보 수정.
        else {
            DetailedHosInformation selectDetailedHosInfo = detailedHosRepository.findById(detailedHosId).orElse(null);

            selectHospital.updateHospital(request.getLicensingDate(), request.getHospitalName(), request.getPhoneNumber(),
                        request.getDistinguishedName(), request.getMedicalSubject(),
                    request.getMedicalSubjectInformation(), request.getBusinessCondition(),
                    request.getCityName());

            selectDetailedHosInfo.updateDetailedHosInformation(request.getNumberWard(),
                    request.getNumberHealthcareProvider(),request.getNumberPatientRoom(),
                    request.getHospitalAddress(),request.getHospitalLocation());
        }
        return selectHospital.getId();
    }
}
