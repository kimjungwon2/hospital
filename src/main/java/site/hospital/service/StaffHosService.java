package site.hospital.service;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.api.dto.staffHospital.StaffHospitalViewResponse;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.hospital.Hospital;
import site.hospital.dto.hospital.staff.StaffModifyStaffHosRequest;
import site.hospital.dto.staffHosInfo.AdminModifyStaffHosRequest;
import site.hospital.repository.HospitalImageRepository;
import site.hospital.repository.StaffHosRepository;
import site.hospital.repository.hospital.HospitalRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffHosService {

    private final StaffHosRepository staffHosRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalImageRepository hospitalImageRepository;
    private final JwtStaffAccessService jwtStaffAccessService;

    //병원 추가 정보 보기(고객)
    public StaffHospitalViewResponse viewStaffHosInfo(Long staffHosId) {
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        return StaffHospitalViewResponse.from(staffHosInformation);
    }

    //병원 관계자 추가 정보 수정
    @Transactional
    public void staffModifyStaffHosInfo(ServletRequest servletRequest, Long staffHosId,
            StaffModifyStaffHosRequest request) {
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        //해당 id 값을 가지고 있는 병원 검색
        Hospital hospital = hospitalRepository.findByStaffHosId(staffHosId);

        jwtStaffAccessService
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
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        //병원 검색
        Hospital hospital = hospitalRepository.findByStaffHosId(staffHosId);

        //토큰의 권한과 authority의 병원 번호가 일치한지 확인.
        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospital.getId());

        //외래키 삭제
        hospital.deleteStaffHosId();

        staffHosRepository.deleteById(staffHosId);
    }

    //병원 추가 정보 삭제
    @Transactional
    public void adminDeleteStaffHosInfo(Long staffHosId) {
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findByStaffHosId(staffHosId);
        hospital.deleteStaffHosId();

        staffHosRepository.deleteById(staffHosId);
    }

    @Transactional
    public void adminModifyStaffHosInfo(Long staffHosId, AdminModifyStaffHosRequest request) {
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(
                        () -> new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));
        StaffHosInformation modifyStaffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        staffHosInformation.modifyStaffHosInformation(modifyStaffHosInformation);
    }

}
