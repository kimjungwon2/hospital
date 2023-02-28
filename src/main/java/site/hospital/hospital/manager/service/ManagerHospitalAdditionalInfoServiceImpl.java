package site.hospital.hospital.manager.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtService;
import site.hospital.hospital.manager.api.dto.ManagerModifyStaffHosRequest;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;
import site.hospital.hospital.user.repository.HospitalRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerHospitalAdditionalInfoServiceImpl implements ManagerHospitalAdditionalInfoService {

    private final HospitalRepository hospitalRepository;
    private final ManagerJwtService managerJwtService;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;


    @Transactional
    @Override
    public void modifyHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long hosAdditionalInfoId,
            ManagerModifyStaffHosRequest request
    ) {
        StaffHosInformation hospitalAdditionalInfo = hospitalAdditionalInfoRepository.findById(hosAdditionalInfoId)
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findHospitalByHosAdditionalInfoId(hosAdditionalInfoId);

        managerJwtService
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
    @Override
    public void deleteHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long memberId,
            Long hosAdditionalInfoId
    ) {
        hospitalAdditionalInfoRepository.findById(hosAdditionalInfoId)
                .orElseThrow(
                        () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findHospitalByHosAdditionalInfoId(hosAdditionalInfoId);

        managerJwtService.accessManager(servletRequest, memberId, hospital.getId());

        deleteHosAdditionalInfoInHospital(hosAdditionalInfoId, hospital);
    }

    private void deleteHosAdditionalInfoInHospital(Long staffHosId, Hospital hospital) {
        hospital.deleteHospitalAdditionalInfo();
        hospitalAdditionalInfoRepository.deleteById(staffHosId);
    }
}
