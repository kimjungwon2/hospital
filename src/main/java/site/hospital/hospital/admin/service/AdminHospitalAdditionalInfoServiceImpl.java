package site.hospital.hospital.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.admin.repository.dto.hospitalAdditionalInfo.AdminModifyStaffHosRequest;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;
import site.hospital.hospital.user.repository.HospitalRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminHospitalAdditionalInfoServiceImpl implements AdminHospitalAdditionalInfoService {

    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional
    @Override
    public void deleteHospitalAdditionalInfo(Long hosAdditionalInfoId) {
        hospitalAdditionalInfoRepository.findById(hosAdditionalInfoId)
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findHospitalByHosAdditionalInfoId(hosAdditionalInfoId);
        hospital.deleteHospitalAdditionalInfo();
        hospitalAdditionalInfoRepository.deleteById(hosAdditionalInfoId);
    }

    @Transactional
    @Override
    public void modifyHospitalAdditionalInfo(
            Long hosAdditionalInfoId,
            AdminModifyStaffHosRequest request
    ) {
        StaffHosInformation hosAdditionalInfo = hospitalAdditionalInfoRepository
                .findById(hosAdditionalInfoId)
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        StaffHosInformation modifiedHospitalAdditionalInfo = StaffHosInformation
                .builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction())
                .build();

        hosAdditionalInfo.modifyHospitalAdditionalInfo(modifiedHospitalAdditionalInfo);
    }

}
