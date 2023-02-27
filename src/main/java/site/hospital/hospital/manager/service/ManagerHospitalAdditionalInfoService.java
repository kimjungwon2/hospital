package site.hospital.hospital.manager.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtAccessService;
import site.hospital.hospital.manager.api.dto.StaffModifyStaffHosRequest;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerHospitalAdditionalInfoService {

    private final HospitalRepository hospitalRepository;
    private final ManagerJwtAccessService managerJwtAccessService;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;


    @Transactional
    public void modifyHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long hosAdditionalInfoId,
            StaffModifyStaffHosRequest request
    ) {
        StaffHosInformation hospitalAdditionalInfo = hospitalAdditionalInfoRepository.findById(hosAdditionalInfoId)
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findHospitalByHosAdditionalInfoId(hosAdditionalInfoId);

        managerJwtAccessService
                .accessManager(servletRequest, request.getMemberId(), hospital.getId());

        StaffHosInformation modifiedHospitalAdditionalInfo = StaffHosInformation
                .builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction())
                .build();

        hospitalAdditionalInfo.modifyHospitalAdditionalInfo(modifiedHospitalAdditionalInfo);
    }

    @Transactional
    public void deleteHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long memberId,
            Long hosAdditionalInfoId
    ) {
        hospitalAdditionalInfoRepository.findById(hosAdditionalInfoId)
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findHospitalByHosAdditionalInfoId(hosAdditionalInfoId);

        managerJwtAccessService.accessManager(servletRequest, memberId, hospital.getId());

        deleteHosAdditionalInfoInHospital(hosAdditionalInfoId, hospital);
    }

    private void deleteHosAdditionalInfoInHospital(Long staffHosId, Hospital hospital) {
        hospital.deleteHospitalAdditionalInfo();
        hospitalAdditionalInfoRepository.deleteById(staffHosId);
    }
}
