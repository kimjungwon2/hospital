package site.hospital.hospital.manager.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtService;
import site.hospital.doctor.manager.domain.Doctor;
import site.hospital.hospital.manager.api.dto.ManagerHospitalView;
import site.hospital.hospital.manager.api.dto.ManagerModifyHospitalRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateHosAdditionalInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.api.dto.HospitalViewImageResponse;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.domain.HospitalImage;
import site.hospital.hospital.user.domain.HospitalThumbnail;
import site.hospital.hospital.user.domain.StaffHosInformation;
import site.hospital.hospital.user.domain.detailedinfo.DetailedHosInformation;
import site.hospital.hospital.user.repository.HospitalDetailedInfoRepository;
import site.hospital.hospital.user.repository.HospitalImageRepository;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.hospital.user.repository.HospitalThumbnailRepository;
import site.hospital.hospital.user.repository.HospitalAdditionalInfoRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerHospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalImageRepository hospitalImageRepository;
    private final ManagerJwtService managerJwtService;
    private final HospitalAdditionalInfoRepository hospitalAdditionalInfoRepository;
    private final HospitalDetailedInfoRepository hospitalDetailedInfoRepository;
    private final HospitalThumbnailRepository hospitalThumbnailRepository;

    public List<HospitalViewImageResponse> viewHospitalImages(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 병원이 존재하지 않습니다."));

        List<HospitalImage> hospitalImages = hospitalImageRepository.findByHospital(hospital);

        List<HospitalViewImageResponse> images =
                hospitalImages
                .stream()
                .map(h -> HospitalViewImageResponse.from(h))
                .collect(Collectors.toList());

        return images;
    }

    public ManagerHospitalView viewHospital(ServletRequest servletRequest) {
        Long JwtHospitalId = managerJwtService.getHospitalNumber(servletRequest);
        Hospital hospital = hospitalRepository.viewHospital(JwtHospitalId);

        Long detailedHosId = checkNullDetailedHospital(hospital);
        Long hosAdditionalInfoId = checkNullHospitalAdditionalInfo(hospital);
        Long hospitalThumbnailId = checkNullHospitalThumbnail(hospital);

        ManagerHospitalView managerHospitalView =
                new ManagerHospitalView(
                hospital,
                detailedHosId,
                hosAdditionalInfoId,
                hospitalThumbnailId);

        return managerHospitalView;
    }

    @Transactional
    public HospitalResponse modifyHospital(
            ServletRequest servletRequest,
            Long hospitalId,
            ManagerModifyHospitalRequest request
    ) {
        Long memberId = request.getMemberId();
        managerJwtService.accessManager(servletRequest, memberId, hospitalId);

        Hospital modifiedHospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));
        
        Hospital hospital = Hospital
                .builder()
                .hospitalName(request.getHospitalName())
                .cityName(request.getCityName())
                .businessCondition(request.getBusinessCondition())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .distinguishedName(request.getDistinguishedName())
                .phoneNumber(request.getPhoneNumber())
                .licensingDate(request.getLicensingDate())
                .build();
        
        modifiedHospital.modifyHospital(hospital);
        
        if (checkModifyDetailedHosInfo(request)) {
            checkMatchDetailedHosInfo(request, modifiedHospital);

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

        return HospitalResponse.from(modifiedHospital.getId());
    }

    @Transactional
    public HospitalResponse registerHosAdditionalInfo(
            ServletRequest servletRequest,
            HospitalManagerCreateHosAdditionalInfoRequest request
    ) {

        StaffHosInformation hospitalAdditionalInfo =
                StaffHosInformation
                .builder()
                .abnormality(request.getAbnormality())
                .consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction())
                .build();

        if (checkDoctorInfo(request)) {
            List<Doctor> doctors = request
                    .getDoctors()
                    .stream()
                    .map(d -> new Doctor(d))
                    .collect(Collectors.toList());
            
            Long HospitalAdditionalInfoId = registerHosAdditionalInfoWithDoctor(
                    servletRequest,
                    request.getMemberId(),
                    request.getHospitalId(),
                    hospitalAdditionalInfo,
                    doctors);
            
            return HospitalResponse.from(HospitalAdditionalInfoId);
        }

        Long HospitalAdditionalInfoId = registerOnlyHosAdditionalInfo(
                servletRequest,
                request.getMemberId(),
                request.getHospitalId(),
                hospitalAdditionalInfo);

        return HospitalResponse.from(HospitalAdditionalInfoId);

    }

    @Transactional
    public HospitalResponse registerDetailedHosInfo(
            ServletRequest servletRequest,
            HospitalManagerCreateDetailHosInfoRequest request
    ) {
        managerJwtService.accessManager(servletRequest, request.getMemberId(),
                request.getHospitalId());

        Hospital hospital = hospitalRepository.findById(request.getHospitalId()).
                orElseThrow(() -> new IllegalStateException("병원이 존재하지 않습니다."));

        checkDetailedHosInfo(hospital);

        DetailedHosInformation detailedHosInformation = DetailedHosInformation
                .builder()
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
    public void deleteDetailedHospitalInfo(
            ServletRequest servletRequest,
            Long memberId,
            Long detailedHosInfoId
    ) {
        DetailedHosInformation detailedHosInformation = hospitalDetailedInfoRepository
                .findById(detailedHosInfoId)
                .orElseThrow(() -> new IllegalStateException("병원 상세 정보가 존재하지 않습니다."));
        
        Hospital hospital = hospitalRepository.findByDetailedHosInformation(detailedHosInformation);

        managerJwtService.accessManager(servletRequest, memberId, hospital.getId());

        deleteDetailedHosInfo(detailedHosInfoId, hospital);
    }

    public HospitalThumbnail viewThumbnail(Long thumbnailId) {
        HospitalThumbnail thumbnail = hospitalThumbnailRepository.findById(thumbnailId)
                .orElseThrow(() -> new IllegalStateException("썸네일이 존재하지 않습니다."));
        
        return thumbnail;
    }

    @Transactional
    protected Long registerHosAdditionalInfoWithDoctor(
            ServletRequest servletRequest,
            Long memberId,
            Long hospitalId,
            StaffHosInformation HospitalAdditionalInfo,
            List<Doctor> doctors
    ) {
        managerJwtService.accessManager(servletRequest, memberId, hospitalId);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("병원이 존재하지 않습니다."));

        checkHospitalAdditionalInfo(hospital);
        insertHosAdditionalInfoInHospital(HospitalAdditionalInfo, doctors, hospital);

        return HospitalAdditionalInfo.getId();
    }


    @Transactional
    protected Long registerOnlyHosAdditionalInfo(
            ServletRequest servletRequest,
            Long memberId,
            Long hospitalId,
            StaffHosInformation staffHosInformation
    ) {

        managerJwtService.accessManager(servletRequest, memberId, hospitalId);

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        checkHospitalAdditionalInfo(hospital);

        hospitalAdditionalInfoRepository.save(staffHosInformation);
        hospital.changeStaffHosInformation(staffHosInformation);

        return staffHosInformation.getId();
    }

    private boolean checkDoctorInfo(HospitalManagerCreateHosAdditionalInfoRequest request) {
        return request.getDoctors() != null;
    }

    private void deleteDetailedHosInfo(Long detailedHosInfoId, Hospital hospital) {
        hospital.deleteDetailedHosId();
        hospitalDetailedInfoRepository.deleteById(detailedHosInfoId);
    }

    private Long checkNullHospitalThumbnail(Hospital hospital) {
        Long hospitalThumbnailId;

        if (hospital.getHospitalThumbnail() == null) {
            hospitalThumbnailId = null;
        } else {
            hospitalThumbnailId = hospital.getHospitalThumbnail().getId();
        }
        return hospitalThumbnailId;
    }

    private Long checkNullHospitalAdditionalInfo(Hospital hospital) {
        Long hospitalAdditionalInfoId;

        if (hospital.getStaffHosInformation() == null) {
            hospitalAdditionalInfoId = null;
        } else {
            hospitalAdditionalInfoId = hospital.getStaffHosInformation().getId();
        }
        return hospitalAdditionalInfoId;
    }

    private Long checkNullDetailedHospital(Hospital hospital) {
        Long detailedHosId;

        if (hospital.getDetailedHosInformation() == null) {
            detailedHosId = null;
        } else {
            detailedHosId = hospital.getDetailedHosInformation().getId();
        }
        return detailedHosId;
    }

    private void checkMatchDetailedHosInfo(ManagerModifyHospitalRequest request, Hospital modifiedHospital) {
        if (modifiedHospital.getDetailedHosInformation().getId() != request
                .getDetailedHosInfoId()) {
            throw new IllegalStateException("DetailedHosInfoId가 일치하지 않습니다.");
        }
    }

    private boolean checkModifyDetailedHosInfo(ManagerModifyHospitalRequest request) {
        return request.getDetailedModifyCheck() == true;
    }

    private void checkDetailedHosInfo(Hospital hospital) {
        if (hospital.getDetailedHosInformation() != null) {
            throw new IllegalStateException("이미 상세 정보가 있습니다.");
        }
    }
    private void insertHosAdditionalInfoInHospital(
            StaffHosInformation HospitalAdditionalInfo,
            List<Doctor> doctors,
            Hospital hospital) {
        StaffHosInformation.createHosAddtionalInfoWithDoctors(HospitalAdditionalInfo, doctors);
        hospitalAdditionalInfoRepository.save(HospitalAdditionalInfo);
        hospital.changeStaffHosInformation(HospitalAdditionalInfo);
    }

    private void checkHospitalAdditionalInfo(Hospital hospital) {
        if (hospital.getStaffHosInformation() != null) {
            throw new IllegalStateException("이미 추가 정보가 있습니다.");
        }
    }

}
