package site.hospital.hospital.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.api.dto.staffHospital.StaffHospitalViewResponse;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalAdditionalInfoService {

    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;


    //병원 추가 정보 보기(고객)
    public StaffHospitalViewResponse viewStaffHosInfo(Long staffHosId) {
        StaffHosInformation staffHosInformation = hospitalAdditionalInfoRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        return StaffHospitalViewResponse.from(staffHosInformation);
    }
}
