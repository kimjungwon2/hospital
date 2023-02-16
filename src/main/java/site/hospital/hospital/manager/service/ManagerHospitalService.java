package site.hospital.hospital.manager.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtAccessService;
import site.hospital.doctor.manager.domain.Doctor;
import site.hospital.hospital.manager.repository.dto.StaffHospitalView;
import site.hospital.hospital.manager.repository.dto.StaffModifyHospitalRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateStaffHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.api.dto.HospitalViewImageResponse;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.HospitalImage;
import site.hospital.hospital.user.domain.HospitalThumbnail;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.hospital.user.repository.HospitalDetailedInfoRepository;
import site.hospital.hospital.user.repository.HospitalImageRepository;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.hospital.user.repository.HospitalThumbnailRepository;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;
import site.hospital.hospital.user.repository.dto.ModifyHospitalRequest;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerHospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalImageRepository hospitalImageRepository;
    private final ManagerJwtAccessService managerJwtAccessService;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;
    private final HospitalDetailedInfoRepository hospitalDetailedInfoRepository;
    private final HospitalThumbnailRepository hospitalThumbnailRepository;

    //관리자 병원 이미지들 보기
    public List<HospitalViewImageResponse> viewHospitalImages(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 병원이 존재하지 않습니다."));

        List<HospitalImage> hospitalImage = hospitalImageRepository.findByHospital(hospital);

        List<HospitalViewImageResponse> images = hospitalImage.stream()
                .map(h -> HospitalViewImageResponse.from(h))
                .collect(Collectors.toList());

        return images;
    }

    //병원 관계자 병원 보기
    public StaffHospitalView staffViewHospital(ServletRequest servletRequest) {
        Long JwtHospitalId = managerJwtAccessService.getHospitalNumber(servletRequest);
        Hospital hospital = hospitalRepository.viewHospital(JwtHospitalId);

        //NullPointerException 방지.
        Long detailedHosId;
        Long staffHosId;
        Long hospitalThumbnailId;

        if (hospital.getDetailedHosInformation() == null) {
            detailedHosId = null;
        } else {
            detailedHosId = hospital.getDetailedHosInformation().getId();
        }

        if (hospital.getStaffHosInformation() == null) {
            staffHosId = null;
        } else {
            staffHosId = hospital.getStaffHosInformation().getId();
        }

        if (hospital.getHospitalThumbnail() == null) {
            hospitalThumbnailId = null;
        } else {
            hospitalThumbnailId = hospital.getHospitalThumbnail().getId();
        }

        StaffHospitalView staffHospitalView = new StaffHospitalView(
                hospital,
                detailedHosId,
                staffHosId,
                hospitalThumbnailId);

        return staffHospitalView;
    }

    //병원 관계자 병원 수정하기
    @Transactional
    public HospitalResponse staffUpdateHospital(
            ServletRequest servletRequest,
            Long hospitalId,
            StaffModifyHospitalRequest request
    ) {
        Long memberId = request.getMemberId();

        managerJwtAccessService.staffAccessFunction(servletRequest, memberId, hospitalId);

        Hospital modifyHospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        Hospital hospital = Hospital.builder().hospitalName(request.getHospitalName())
                .cityName(request.getCityName()).businessCondition(request.getBusinessCondition())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .distinguishedName(request.getDistinguishedName())
                .phoneNumber(request.getPhoneNumber()).licensingDate(request.getLicensingDate())
                .build();

        modifyHospital.modifyHospital(hospital);

        //병원 추가정보 수정 유무
        if (request.getDetailedModifyCheck() == true) {
            //detailed hospitalId가 일치하지 않으면 수정 취소.
            if (modifyHospital.getDetailedHosInformation().getId() != request
                    .getDetailedHosInfoId()) {
                throw new IllegalStateException("DetailedHosInfoId가 일치하지 않습니다.");
            }

            DetailedHosInformation detailedHosInformation = hospitalDetailedInfoRepository
                    .findById(request.getDetailedHosInfoId())
                    .orElseThrow(
                            () -> new IllegalStateException("해당 id에 속하는 상세 병원 정보가 존재하지 않습니다."));

            DetailedHosInformation modifyDetailedHosInformation = DetailedHosInformation.builder()
                    .numberPatientRoom(request.getNumberPatientRoom())
                    .numberWard(request.getNumberWard())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider())
                    .hospitalAddress(request.getHospitalAddress())
                    .hospitalLocation(request.getHospitalLocation()).build();

            detailedHosInformation.modifyDetailedHosInformation(modifyDetailedHosInformation);
        }
        return HospitalResponse.from(modifyHospital.getId());
    }


    //병원 관계자 병원 추가정보 + 의사 등록
    @Transactional
    public Long staffRegisterStaffHosInformation(ServletRequest servletRequest, Long memberId,
            Long hospitalId,
            StaffHosInformation staffHosInformation,
            List<Doctor> doctors) {

        managerJwtAccessService.staffAccessFunction(servletRequest, memberId, hospitalId);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }

        StaffHosInformation.createStaffHosInformation(staffHosInformation, doctors);
        hospitalAdditionalInfoRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
    }


    //병원 관계자 병원 추가 정보 등록
    @Transactional
    public HospitalResponse staffCreateStaffHosInfo(
            ServletRequest servletRequest,
            HospitalManagerCreateStaffHosInfoRequest request
    ) {

        StaffHosInformation staffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        //의사 정보가 있어야지 의사 추가.
        if (request.getDoctors() != null) {
            List<Doctor> doctors = request.getDoctors().stream().map(d -> new Doctor(d))
                    .collect(Collectors.toList());
            Long id = staffRegisterStaffHosInformation(servletRequest, request.getMemberId(),
                    request.getHospitalId(), staffHosInformation, doctors);
            return HospitalResponse.from(id);
        }
        //추가 정보만 추가.
        else {
            Long id = staffRegisterStaffHosInfo(servletRequest, request.getMemberId(),
                    request.getHospitalId(), staffHosInformation);
            return HospitalResponse.from(id);
        }

    }


    //병원 관계자 병원 추가정보만 등록.
    @Transactional
    public Long staffRegisterStaffHosInfo(ServletRequest servletRequest, Long memberId,
            Long hospitalId,
            StaffHosInformation staffHosInformation) {

        managerJwtAccessService.staffAccessFunction(servletRequest, memberId, hospitalId);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }

        hospitalAdditionalInfoRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
    }



    //병원 관계자 추가 정보 등록하기
    @Transactional
    public HospitalResponse staffRegisterDetailHospitalInformation(
            ServletRequest servletRequest,
            HospitalManagerCreateDetailHosInfoRequest request
    ) {
        DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                .numberPatientRoom(request.getNumberPatientRoom())
                .numberWard(request.getNumberWard())
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .hospitalLocation(request.getHospitalLocation())
                .hospitalAddress(request.getHospitalAddress()).build();

        managerJwtAccessService.staffAccessFunction(servletRequest, request.getMemberId(),
                request.getHospitalId());

        Hospital hospital = hospitalRepository.findById(request.getHospitalId()).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getDetailedHosInformation() != null) {
            throw new IllegalStateException("이미 상세 정보가 있습니다.");
        }

        hospital.changeDetailedHosInformation(detailedHosInformation);
        hospitalDetailedInfoRepository.save(detailedHosInformation);

        return HospitalResponse.from(detailedHosInformation.getId());
    }


    //병원 관계자 상세 정보 삭제하기
    @Transactional
    public void staffDeleteDetailHospitalInformation(ServletRequest servletRequest, Long memberId,
            Long detailedHosInfoId) {
        DetailedHosInformation detailedHosInformation = hospitalDetailedInfoRepository
                .findById(detailedHosInfoId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 상세 정보가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findByDetailedHosInformation(detailedHosInformation);

        managerJwtAccessService.staffAccessFunction(servletRequest, memberId, hospital.getId());

        hospital.deleteDetailedHosId();

        hospitalDetailedInfoRepository.deleteById(detailedHosInfoId);
    }


    //병원 + 상세 정보 수정 (미사용 상태)
    @Transactional
    public Long modifyAllHosInformation(Long hospitalId, Long detailedHosId,
            ModifyHospitalRequest request) {
        //병원 정보만 수정.
        Hospital selectHospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        if (detailedHosId == null) {

            selectHospital.updateHospital(request.getLicensingDate(), request.getHospitalName(),
                    request.getPhoneNumber(),
                    request.getDistinguishedName(),
                    request.getMedicalSubjectInformation(), request.getBusinessCondition(),
                    request.getCityName());
        }
        //병원 + 상세 정보 수정.
        else {
            DetailedHosInformation selectDetailedHosInfo = hospitalDetailedInfoRepository
                    .findById(detailedHosId)
                    .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 상세 정보가 존재하지 않습니다."));

            selectHospital.updateHospital(request.getLicensingDate(), request.getHospitalName(),
                    request.getPhoneNumber(),
                    request.getDistinguishedName(),
                    request.getMedicalSubjectInformation(), request.getBusinessCondition(),
                    request.getCityName());

            selectDetailedHosInfo.updateDetailedHosInformation(request.getNumberWard(),
                    request.getNumberHealthcareProvider(), request.getNumberPatientRoom(),
                    request.getHospitalAddress(), request.getHospitalLocation());
        }
        return selectHospital.getId();
    }

    //관리자 병원 섬네일 보기
    public HospitalThumbnail viewThumbnail(Long thumbnailId) {
        HospitalThumbnail thumbnail = hospitalThumbnailRepository.findById(thumbnailId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 썸네일이 존재하지 않습니다."));
        return thumbnail;
    }

}
