package site.hospital.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.hospital.api.dto.hospital.HospitalCreateDetailedHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalCreateRequest;
import site.hospital.api.dto.hospital.HospitalCreateStaffHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalManagerCreateStaffHosInfoRequest;
import site.hospital.api.dto.hospital.HospitalResponse;
import site.hospital.api.dto.hospital.HospitalViewImageResponse;
import site.hospital.domain.Doctor;
import site.hospital.domain.HospitalImage;
import site.hospital.domain.HospitalThumbnail;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.domain.hospital.Hospital;
import site.hospital.dto.AdminHospitalSearchCondition;
import site.hospital.dto.ModifyHospitalRequest;
import site.hospital.dto.hospital.admin.AdminHospitalView;
import site.hospital.dto.hospital.admin.AdminModifyHospitalRequest;
import site.hospital.dto.hospital.staff.StaffHospitalView;
import site.hospital.dto.hospital.staff.StaffModifyHospitalRequest;
import site.hospital.repository.DetailedHosRepository;
import site.hospital.repository.HospitalImageRepository;
import site.hospital.repository.HospitalThumbnailRepository;
import site.hospital.repository.StaffHosRepository;
import site.hospital.repository.estimation.EstimationRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.hospital.adminSearchQuery.AdminHospitalSearchRepository;
import site.hospital.repository.hospital.adminSearchQuery.AdminSearchHospitalDto;
import site.hospital.repository.hospital.searchQuery.HospitalSearchDto;
import site.hospital.repository.hospital.searchQuery.HospitalSearchRepository;
import site.hospital.repository.hospital.viewQuery.HospitalViewRepository;
import site.hospital.repository.hospital.viewQuery.ViewHospitalDTO;
import site.hospital.repository.question.QuestionRepository;
import site.hospital.repository.review.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final StaffHosRepository staffHosRepository;
    private final HospitalSearchRepository hospitalSearchRepository;
    private final HospitalViewRepository hospitalViewRepository;
    private final AdminHospitalSearchRepository adminHospitalSearchRepository;
    private final DetailedHosRepository detailedHosRepository;
    private final HospitalThumbnailRepository hospitalThumbnailRepository;
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;
    private final EstimationRepository estimationRepository;
    private final JwtStaffAccessService jwtStaffAccessService;
    private final HospitalImageRepository hospitalImageRepository;


    //병원 + 상세 정보등록
    @Transactional
    public Long register(Hospital hospital, DetailedHosInformation detailedHosInformation) {

        hospital.changeDetailedHosInformation(detailedHosInformation);
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    public HospitalResponse adminSaveHospital(HospitalCreateRequest request) {
        Hospital hospital = Hospital.builder()
                .licensingDate(request.getLicensingDate())
                .hospitalName(request.getHospitalName())
                .phoneNumber(request.getPhoneNumber())
                .distinguishedName(request.getDistinguishedName())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .businessCondition(request.getBusinessCondition())
                .cityName(request.getCityName())
                .build();

        //상세정보 체크를 기입하지 않을 경우
        if (request.getDetailedInfoCheck() == null) {
            throw new IllegalStateException("상세 정보 체크를 하세요.");
        }

        //상세정보를 체크하지 않을 경우 hospital 정보만 저장.
        if (request.getDetailedInfoCheck() == false) {
            Long id = registerHospital(hospital);
            return HospitalResponse.from(id);
        }
        //상세 정보를 체크할 경우 hospital + 상세정보 저장.
        else if (request.getDetailedInfoCheck() == true &&
                request.getHospitalLocation() != null
                && request.getHospitalLocation().getLatitude() != null
                && request.getHospitalLocation().getLongitude() != null
                && request.getHospitalLocation().getX_coordination() != null
                && request.getHospitalLocation().getX_coordination() != null
                && request.getHospitalLocation().getY_coordination() != null
                && request.getHospitalAddress() != null
                && request.getHospitalAddress().getLandLotBasedSystem() != null
                && request.getHospitalAddress().getRoadBaseAddress() != null
                && request.getHospitalAddress().getZipCode() != null
                && request.getNumberHealthcareProvider() != null && request.getNumberWard() != null
                && request.getNumberPatientRoom() != null) {
            DetailedHosInformation detailedHosInformation = DetailedHosInformation.builder()
                    .numberWard(request.getNumberWard())
                    .numberPatientRoom(request.getNumberPatientRoom())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider())
                    .hospitalLocation(request.getHospitalLocation())
                    .hospitalAddress(request.getHospitalAddress()).build();

            Long id = register(hospital, detailedHosInformation);

            return HospitalResponse.from(id);
        }
        //상세 정보를 체크했는데도 상세 정보를 모두 기입 안 할 경우
        else {
            throw new IllegalStateException("상세 정보를 모두 기입하세요");
        }
    }

    //병원만 등록.
    @Transactional
    public Long registerHospital(Hospital hospital) {
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    //병원 검색
    public Page<HospitalSearchDto> searchHospital(String searchName, Pageable pageable) {
        return hospitalSearchRepository.searchHospital(searchName, pageable);
    }

    //병원 관계자 병원 보기
    public StaffHospitalView staffViewHospital(ServletRequest servletRequest) {
        Long JwtHospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);
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

        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospitalId);

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

            DetailedHosInformation detailedHosInformation = detailedHosRepository
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

        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospitalId);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }

        StaffHosInformation.createStaffHosInformation(staffHosInformation, doctors);
        staffHosRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
    }

    //병원 관계자 병원 추가 정보 등록
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

        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospitalId);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }

        staffHosRepository.save(staffHosInformation);

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

        jwtStaffAccessService.staffAccessFunction(servletRequest, request.getMemberId(),
                request.getHospitalId());

        Hospital hospital = hospitalRepository.findById(request.getHospitalId()).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getDetailedHosInformation() != null) {
            throw new IllegalStateException("이미 상세 정보가 있습니다.");
        }

        hospital.changeDetailedHosInformation(detailedHosInformation);
        detailedHosRepository.save(detailedHosInformation);

        return HospitalResponse.from(detailedHosInformation.getId());
    }

    //병원 관계자 상세 정보 삭제하기
    @Transactional
    public void staffDeleteDetailHospitalInformation(ServletRequest servletRequest, Long memberId,
            Long detailedHosInfoId) {
        DetailedHosInformation detailedHosInformation = detailedHosRepository
                .findById(detailedHosInfoId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 상세 정보가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findByDetailedHosInformation(detailedHosInformation);

        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospital.getId());

        hospital.deleteDetailedHosId();

        detailedHosRepository.deleteById(detailedHosInfoId);
    }


    //관리자 병원 추가정보 +의사 등록
    @Transactional
    public Long adminRegisterStaffHosInformation(Long hospitalId,
            StaffHosInformation staffHosInformation,
            List<Doctor> doctors) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }

        StaffHosInformation.createStaffHosInformation(staffHosInformation, doctors);
        staffHosRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
    }

    //관리자 병원 추가정보만 등록.
    @Transactional
    public Long adminRegisterStaffHosInfo(Long hospitalId,
            StaffHosInformation staffHosInformation) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }

        staffHosRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
    }

    //관리자 병원 추가 정보 등록
    public HospitalResponse adminCreateStaffHosInfo(HospitalCreateStaffHosInfoRequest request) {

        StaffHosInformation staffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        //의사 정보가 있어야지 의사 추가.
        if (request.getDoctors() != null) {
            List<Doctor> doctors = request.getDoctors()
                    .stream()
                    .map(d -> new Doctor(d))
                    .collect(Collectors.toList());

            Long id = adminRegisterStaffHosInformation(request.getHospitalId(), staffHosInformation,
                            doctors);
            return HospitalResponse.from(id);
        }
        //추가 정보만 추가.
        Long id = adminRegisterStaffHosInfo(request.getHospitalId(), staffHosInformation);

        return HospitalResponse.from(id);
    }

    //병원 + 상세 정보 수정
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
            DetailedHosInformation selectDetailedHosInfo = detailedHosRepository
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

    //병원 정보 상세 보기
    public ViewHospitalDTO viewHospital(Long hospitalId) {
        ViewHospitalDTO hospital = hospitalViewRepository.viewHospital(hospitalId);
        if (hospital == null) {
            throw new IllegalStateException("해당 병원은 존재하지 않습니다.");
        }

        return hospital;
    }

    //상세 정보 등록하기
    @Transactional
    public HospitalResponse registerDetailHospitalInformation(
            HospitalCreateDetailedHosInfoRequest request
            , Long hospitalId
    ) {
        Hospital hospital = hospitalRepository.findById(hospitalId).
                orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if (hospital.getDetailedHosInformation() != null) {
            throw new IllegalStateException("이미 상세 정보가 있습니다.");
        }

        DetailedHosInformation detailedHosInformation =
                DetailedHosInformation.builder()
                .numberPatientRoom(request.getNumberPatientRoom())
                .numberWard(request.getNumberWard())
                .numberHealthcareProvider(request.getNumberHealthcareProvider())
                .hospitalLocation(request.getHospitalLocation())
                .hospitalAddress(request.getHospitalAddress())
                .build();

        hospital.changeDetailedHosInformation(detailedHosInformation);
        detailedHosRepository.save(detailedHosInformation);

        return HospitalResponse.from(detailedHosInformation.getId());
    }

    //상세 정보 삭제하기
    @Transactional
    public void deleteDetailHospitalInformation(Long detailedHosInfoId) {
        DetailedHosInformation detailedHosInformation = detailedHosRepository
                .findById(detailedHosInfoId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 상세 정보가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findByDetailedHosInformation(detailedHosInformation);
        hospital.deleteDetailedHosId();

        detailedHosRepository.deleteById(detailedHosInfoId);
    }

    //관리자 병원 검색
    public Page<AdminSearchHospitalDto> adminSearchHospitals(
            Long hospitalId,
            String hospitalName,
            BusinessCondition businessCondition,
            String cityName,
            Pageable pageable
    ) {
        AdminHospitalSearchCondition condition = AdminHospitalSearchCondition.builder()
                .hospitalId(hospitalId).hospitalName(hospitalName)
                .businessCondition(businessCondition).cityName(cityName).build();

        return adminHospitalSearchRepository.adminSearchHospitals(condition, pageable);
    }

    //관리자 병원 보기
    public AdminHospitalView adminViewHospital(Long hospitalId,Long detailedHosInfoId,Long staffHosInfoId,Long thumbnailId) {
        Hospital hospital = hospitalRepository.viewHospital(hospitalId);

        AdminHospitalView adminHospitalView = new AdminHospitalView(hospital, detailedHosInfoId,
                staffHosInfoId, thumbnailId);

        return adminHospitalView;
    }

    //관리자 병원 삭제하기
    @Transactional
    public void adminDeleteHospital(Long hospitalId, Long staffHosId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //병원 STAFF 정보도 삭제.
        if (staffHosId != null) {
            if (hospital.getStaffHosInformation().getId() != staffHosId) {
                throw new IllegalStateException("해당 병원과 staffId가 일치하지 않습니다.");
            }

            StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                    .orElseThrow(
                            () -> new IllegalStateException("해당 id에 속하는 병원 추가 정보가 존재하지 않습니다."));
            staffHosRepository.deleteById(staffHosId);
        }

        //병원과 연관된 review, question, estimation 삭제.
        reviewRepository.adminDeleteReviewHospital(hospital);
        questionRepository.adminDeleteQuestion(hospital);
        estimationRepository.adminDeleteEstimation(hospital);

        hospitalRepository.deleteById(hospitalId);
    }

    //관리자 병원 수정하기
    @Transactional
    public HospitalResponse adminUpdateHospital(Long hospitalId, AdminModifyHospitalRequest request) {
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

            DetailedHosInformation detailedHosInformation = detailedHosRepository
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

    //관리자 병원 섬네일 보기
    public HospitalThumbnail viewThumbnail(Long thumbnailId) {
        HospitalThumbnail thumbnail = hospitalThumbnailRepository.findById(thumbnailId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 썸네일이 존재하지 않습니다."));
        return thumbnail;
    }

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
}
