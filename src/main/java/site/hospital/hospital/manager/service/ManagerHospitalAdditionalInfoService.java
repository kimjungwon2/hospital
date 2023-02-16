package site.hospital.hospital.manager.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtAccessService;
import site.hospital.hospital.manager.repository.dto.StaffModifyStaffHosRequest;
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


    //병원 관계자 추가 정보 수정
    @Transactional
    public void staffModifyStaffHosInfo(ServletRequest servletRequest, Long staffHosId,
            StaffModifyStaffHosRequest request) {
        StaffHosInformation staffHosInformation = hospitalAdditionalInfoRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        //해당 id 값을 가지고 있는 병원 검색
        Hospital hospital = hospitalRepository.findByStaffHosId(staffHosId);

        managerJwtAccessService
                .staffAccessFunction(servletRequest, request.getMemberId(), hospital.getId());

        StaffHosInformation modifyStaffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        staffHosInformation.modifyStaffHosInformation(modifyStaffHosInformation);
    }

    //병원 관계자 추가 정보 삭제
    @Transactional
    public void staffDeleteStaffHosInfo(ServletRequest servletRequest, Long memberId,
            Long staffHosId) {
        StaffHosInformation staffHosInformation = hospitalAdditionalInfoRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        //병원 검색
        Hospital hospital = hospitalRepository.findByStaffHosId(staffHosId);

        //토큰의 권한과 authority의 병원 번호가 일치한지 확인.
        managerJwtAccessService.staffAccessFunction(servletRequest, memberId, hospital.getId());

        //외래키 삭제
        hospital.deleteStaffHosId();

        hospitalAdditionalInfoRepository.deleteById(staffHosId);
    }
}
