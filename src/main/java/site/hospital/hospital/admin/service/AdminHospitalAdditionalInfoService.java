package site.hospital.hospital.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;
import site.hospital.hospital.admin.repository.dto.hospitalAdditionalInfo.AdminModifyStaffHosRequest;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminHospitalAdditionalInfoService {

    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;
    private final HospitalRepository hospitalRepository;

    //병원 추가 정보 삭제
    @Transactional
    public void adminDeleteStaffHosInfo(Long staffHosId) {
        hospitalAdditionalInfoRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findHospitalByHosAdditionalInfoId(staffHosId);
        hospital.deleteHospitalAdditionalInfo();

        hospitalAdditionalInfoRepository.deleteById(staffHosId);
    }

    @Transactional
    public void adminModifyStaffHosInfo(Long staffHosId, AdminModifyStaffHosRequest request) {
        StaffHosInformation staffHosInformation = hospitalAdditionalInfoRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));
        StaffHosInformation modifyStaffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        staffHosInformation.modifyHospitalAdditionalInfo(modifyStaffHosInformation);
    }


}
