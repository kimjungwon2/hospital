package site.hospital.hospital.admin.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.doctor.manager.domain.Doctor;
import site.hospital.estimation.admin.repository.EstimationRepository;
import site.hospital.hospital.admin.repository.dto.AdminHospitalView;
import site.hospital.hospital.admin.repository.dto.AdminModifyHospitalRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateDetailedHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateStaffHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.domain.BusinessCondition;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.hospital.user.repository.HospitalDetailedInfoRepository;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;
import site.hospital.hospital.admin.repository.adminSearchQuery.AdminHospitalSearchRepository;
import site.hospital.hospital.admin.repository.adminSearchQuery.AdminSearchHospitalDto;
import site.hospital.hospital.admin.repository.dto.AdminHospitalSearchCondition;
import site.hospital.question.user.repository.QuestionRepository;
import site.hospital.review.user.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminHospitalService {

    private final HospitalRepository hospitalRepository;
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;
    private final EstimationRepository estimationRepository;
    private final AdminHospitalSearchRepository adminHospitalSearchRepository;
    private final HospitalDetailedInfoRepository hospitalDetailedInfoRepository;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;


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

            StaffHosInformation staffHosInformation = hospitalAdditionalInfoRepository.findById(staffHosId)
                    .orElseThrow(
                            () -> new IllegalStateException("해당 id에 속하는 병원 추가 정보가 존재하지 않습니다."));
            hospitalAdditionalInfoRepository.deleteById(staffHosId);
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
        hospitalAdditionalInfoRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
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

    @Transactional
    public Long registerHospital(Hospital hospital) {
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    //병원 + 상세 정보등록
    @Transactional
    public Long register(Hospital hospital, DetailedHosInformation detailedHosInformation) {

        hospital.changeDetailedHosInformation(detailedHosInformation);
        hospitalRepository.save(hospital);

        return hospital.getId();
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

        hospitalAdditionalInfoRepository.save(staffHosInformation);

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

    //상세 정보 삭제하기
    @Transactional
    public void deleteDetailHospitalInformation(Long detailedHosInfoId) {
        DetailedHosInformation detailedHosInformation = hospitalDetailedInfoRepository
                .findById(detailedHosInfoId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 상세 정보가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findByDetailedHosInformation(detailedHosInformation);
        hospital.deleteDetailedHosId();

        hospitalDetailedInfoRepository.deleteById(detailedHosInfoId);
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
        hospitalDetailedInfoRepository.save(detailedHosInformation);

        return HospitalResponse.from(detailedHosInformation.getId());
    }


}
