package site.hospital.hospital.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.api.dto.additionalinfo.HospitalAdditionalInfoViewResponse;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalAdditionalInfoService {

    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;


    public HospitalAdditionalInfoViewResponse viewHospitalAdditionalInfo(Long hosAdditionalInfoId) {
        StaffHosInformation hospitalAdditionalInfo = hospitalAdditionalInfoRepository.findById(hosAdditionalInfoId)
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        return HospitalAdditionalInfoViewResponse.from(hospitalAdditionalInfo);
    }
}
