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
import site.hospital.hospital.admin.repository.dto.AdminHospitalSearchCondition;
import site.hospital.hospital.admin.repository.dto.view.AdminHospitalView;
import site.hospital.hospital.admin.repository.dto.AdminModifyHospitalRequest;
import site.hospital.hospital.admin.repository.search.AdminHospitalSearchRepository;
import site.hospital.hospital.admin.repository.search.AdminHospitalSearchSelectQuery;
import site.hospital.hospital.user.api.dto.HospitalCreateDetailedHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateStaffHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.domain.BusinessCondition;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.domain.detailedinfo.DetailedHosInformation;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;
import site.hospital.hospital.user.repository.HospitalDetailedInfoRepository;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.question.user.repository.QuestionRepository;
import site.hospital.review.user.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminHospitalServiceImpl implements AdminHospitalService {

    private final HospitalRepository hospitalRepository;
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;
    private final EstimationRepository estimationRepository;
    private final AdminHospitalSearchRepository adminHospitalSearchRepository;
    private final HospitalDetailedInfoRepository hospitalDetailedInfoRepository;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;

    private static final String HOSPITAL_NOT_EXISTS= "병원이 존재하지 않습니다.";


    @Override
    public Page<AdminHospitalSearchSelectQuery> searchHospitals(
            Long hospitalId,
            String hospitalName,
            BusinessCondition businessCondition,
            String cityName,
            Pageable pageable
    ) {
        AdminHospitalSearchCondition searchCondition = AdminHospitalSearchCondition
                .builder()
                .hospitalId(hospitalId)
                .hospitalName(hospitalName)
                .businessCondition(businessCondition)
                .cityName(cityName)
                .build();

        return adminHospitalSearchRepository.adminSearchHospitals(searchCondition, pageable);
    }

    @Override
    public AdminHospitalView viewHospital(
            Long hospitalId,
            Long detailedHosInfoId,
            Long staffHosInfoId,
            Long thumbnailId
    ) {
        Hospital hospital = hospitalRepository.viewHospital(hospitalId);

        return new AdminHospitalView(
                hospital,
                detailedHosInfoId,
                staffHosInfoId,
                thumbnailId);
    }

    @Transactional
    @Override
    public void deleteHospital(Long hospitalId, Long hosAdditionalInfoId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException(HOSPITAL_NOT_EXISTS));

        deleteHospitalAdditionalInfo(hosAdditionalInfoId, hospital);

        reviewRepository.adminDeleteReviewHospital(hospital);
        questionRepository.adminDeleteQuestion(hospital);
        estimationRepository.adminDeleteEstimation(hospital);

        hospitalRepository.deleteById(hospitalId);
    }

    @Transactional
    @Override
    public HospitalResponse modifyHospital(Long hospitalId, AdminModifyHospitalRequest request) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException(HOSPITAL_NOT_EXISTS));

        Hospital modifiedHospital = Hospital
                .builder()
                .hospitalName(request.getHospitalName())
                .cityName(request.getCityName())
                .businessCondition(request.getBusinessCondition())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .distinguishedName(request.getDistinguishedName())
                .phoneNumber(request.getPhoneNumber())
                .licensingDate(request.getLicensingDate())
                .build();

        hospital.modifyHospital(modifiedHospital);

        if (checkModifyDetailedHosInfo(request)) {
            checkMatchDetailedHosInfo(request, hospital);

            DetailedHosInformation detailedHosInformation = hospitalDetailedInfoRepository
                    .findById(request.getDetailedHosInfoId())
                    .orElseThrow(
                            () -> new IllegalStateException("상세 병원 정보가 존재하지 않습니다."));

            DetailedHosInformation modifyDetailedHosInformation = DetailedHosInformation
                    .builder()
                    .numberPatientRoom(request.getNumberPatientRoom())
                    .numberWard(request.getNumberWard())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider())
                    .hospitalAddress(request.getHospitalAddress())
                    .hospitalLocation(request.getHospitalLocation())
                    .build();

            detailedHosInformation.modifyDetailedHosInformation(modifyDetailedHosInformation);
        }

        return HospitalResponse.from(hospital.getId());
    }

    @Override
    public HospitalResponse registerHospital(HospitalCreateRequest request) {
        Hospital hospital = Hospital
                .builder()
                .licensingDate(request.getLicensingDate())
                .hospitalName(request.getHospitalName())
                .phoneNumber(request.getPhoneNumber())
                .distinguishedName(request.getDistinguishedName())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .businessCondition(request.getBusinessCondition())
                .cityName(request.getCityName())
                .build();

        checkEditDetailedHosInfo(request);

        if (checkDetailedHosInfoFalse(request)) {
            Long hospitalId = registerHospital(hospital);

            return HospitalResponse.from(hospitalId);
        }
        else if (checkDetailedHosInfoTrue(request)) {
            DetailedHosInformation detailedHosInformation = DetailedHosInformation
                    .builder()
                    .numberWard(request.getNumberWard())
                    .numberPatientRoom(request.getNumberPatientRoom())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider())
                    .hospitalLocation(request.getHospitalLocation())
                    .hospitalAddress(request.getHospitalAddress())
                    .build();

            Long hospitalId = registerHospitalWithDetailedHosInfo(
                    hospital,
                    detailedHosInformation);

            return HospitalResponse.from(hospitalId);
        }
        else {
            throw new IllegalStateException("상세 정보를 모두 기입하세요");
        }
    }

    @Override
    public HospitalResponse registerHosAdditionalInfo(HospitalCreateStaffHosInfoRequest request) {

        StaffHosInformation hospitalAdditionalInfo = StaffHosInformation
                .builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction())
                .build();

        if (checkDoctorInfo(request)) {
            List<Doctor> doctors = request
                    .getDoctors()
                    .stream()
                    .map(Doctor::new)
                    .collect(Collectors.toList());

            Long hospitalAdditionalInfoId = registerHosAdditionalInfoWithDoctor(
                    request.getHospitalId(),
                    hospitalAdditionalInfo,
                    doctors);

            return HospitalResponse.from(hospitalAdditionalInfoId);
        }

        Long hospitalAdditionalInfoId = registerOnlyHosAdditionalInfo(
                request.getHospitalId(),
                hospitalAdditionalInfo);

        return HospitalResponse.from(hospitalAdditionalInfoId);
    }

    @Transactional
    @Override
    public void deleteDetailedHospitalInfo(Long detailedHosInfoId) {
        DetailedHosInformation detailedHosInformation = hospitalDetailedInfoRepository
                .findById(detailedHosInfoId)
                .orElseThrow(() -> new IllegalStateException("병원 상세 정보가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findByDetailedHosInformation(detailedHosInformation);

        deleteDetailedHosInfo(detailedHosInfoId, hospital);
    }

    @Transactional
    @Override
    public HospitalResponse registerDetailedHosInfo(
            HospitalCreateDetailedHosInfoRequest request
            , Long hospitalId
    ) {
        Hospital hospital = hospitalRepository.findById(hospitalId).
                orElseThrow(() -> new IllegalStateException(HOSPITAL_NOT_EXISTS));

        checkDetailedHosInfo(hospital);

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

    @Transactional
    protected Long registerOnlyHosAdditionalInfo(
            Long hospitalId,
            StaffHosInformation staffHosInformation
    ) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException(HOSPITAL_NOT_EXISTS));

        checkHospitalAdditionalInfo(hospital);

        hospitalAdditionalInfoRepository.save(staffHosInformation);
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
    }

    @Transactional
    protected Long registerHospital(Hospital hospital) {
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    @Transactional
    protected Long registerHospitalWithDetailedHosInfo(Hospital hospital, DetailedHosInformation detailedHosInformation) {

        hospital.changeDetailedHosInformation(detailedHosInformation);
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    @Transactional
    protected Long registerHosAdditionalInfoWithDoctor(
            Long hospitalId,
            StaffHosInformation hospitalAdditionalInfo,
            List<Doctor> doctors
    ) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException(HOSPITAL_NOT_EXISTS));

        checkHospitalAdditionalInfo(hospital);

        insertHosAdditionalInfoInHospital(hospitalAdditionalInfo, doctors, hospital);

        return hospitalAdditionalInfo.getId();
    }

    private void checkDetailedHosInfo(Hospital hospital) {
        if (hospital.getDetailedHosInformation() != null) {
            throw new IllegalStateException("이미 상세 정보가 있습니다.");
        }
    }

    private void deleteHospitalAdditionalInfo(Long hosAdditionalInfoId, Hospital hospital) {
        if (hosAdditionalInfoId != null) {
            checkHosAdditionalInfoMatch(hosAdditionalInfoId, hospital);

            hospitalAdditionalInfoRepository.findById(hosAdditionalInfoId)
                    .orElseThrow(
                            () -> new IllegalStateException("병원 추가 정보가 존재하지 않습니다."));

            hospitalAdditionalInfoRepository.deleteById(hosAdditionalInfoId);
        }
    }

    private void checkHosAdditionalInfoMatch(Long hosAdditionalInfoId, Hospital hospital) {
        if (!hospital.getStaffHosInformation().getId().equals(hosAdditionalInfoId)) {
            throw new IllegalStateException("해당 병원과 staffId가 일치하지 않습니다.");
        }
    }

    private void checkMatchDetailedHosInfo(AdminModifyHospitalRequest request, Hospital hospital) {
        if (!hospital.getDetailedHosInformation().getId()
                .equals(request.getDetailedHosInfoId())) {
            throw new IllegalStateException("DetailedHosInfoId가 일치하지 않습니다.");
        }
    }

    private boolean checkModifyDetailedHosInfo(AdminModifyHospitalRequest request) {
        return request.getDetailedModifyCheck();
    }

    private void deleteDetailedHosInfo(Long detailedHosInfoId, Hospital hospital) {
        hospital.deleteDetailedHosId();
        hospitalDetailedInfoRepository.deleteById(detailedHosInfoId);
    }

    private void insertHosAdditionalInfoInHospital(
            StaffHosInformation hospitalAdditionalInfo,
            List<Doctor> doctors,
            Hospital hospital
    ) {
        StaffHosInformation.createHosAddtionalInfoWithDoctors(hospitalAdditionalInfo, doctors);
        hospitalAdditionalInfoRepository.save(hospitalAdditionalInfo);
        hospital.changeStaffHosInformation(hospitalAdditionalInfo);
    }

    private void checkHospitalAdditionalInfo(Hospital hospital) {
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }
    }

    private boolean checkDoctorInfo(HospitalCreateStaffHosInfoRequest request) {
        return request.getDoctors() != null;
    }

    private void checkEditDetailedHosInfo(HospitalCreateRequest request) {
        if (request.getDetailedInfoCheck() == null) {
            throw new IllegalStateException("상세 정보 체크를 하세요.");
        }
    }

    private boolean checkDetailedHosInfoTrue(HospitalCreateRequest request) {
        return request.getDetailedInfoCheck() &&
                request.getHospitalLocation() != null
                && request.getHospitalLocation().getLatitude() != null
                && request.getHospitalLocation().getLongitude() != null
                && request.getHospitalLocation().getXCoordination() != null
                && request.getHospitalLocation().getYCoordination() != null
                && request.getHospitalAddress() != null
                && request.getHospitalAddress().getLandLotBasedSystem() != null
                && request.getHospitalAddress().getRoadBaseAddress() != null
                && request.getHospitalAddress().getZipCode() != null
                && request.getNumberHealthcareProvider() != null
                && request.getNumberWard() != null
                && request.getNumberPatientRoom() != null;
    }

    private boolean checkDetailedHosInfoFalse(HospitalCreateRequest request) {
        return request.getDetailedInfoCheck() == false;
    }

}
