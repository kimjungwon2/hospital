package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.domain.Doctor;
import site.hospital.domain.HospitalImage;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.Hospital;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.dto.AdminHospitalSearchCondition;
import site.hospital.dto.AdminModifyHospitalRequest;
import site.hospital.dto.ModifyHospitalRequest;
import site.hospital.repository.DetailedHosRepository;
import site.hospital.repository.HospitalImageRepository;
import site.hospital.repository.estimation.EstimationRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.StaffHosRepository;
import site.hospital.repository.hospital.adminSearchQuery.AdminHospitalSearchRepository;
import site.hospital.repository.hospital.adminSearchQuery.AdminSearchHospitalDto;
import site.hospital.repository.hospital.searchQuery.HospitalSearchDto;
import site.hospital.repository.hospital.searchQuery.HospitalSearchRepository;
import site.hospital.repository.hospital.viewQuery.HospitalViewRepository;
import site.hospital.repository.hospital.viewQuery.ViewHospitalDTO;
import site.hospital.repository.question.QuestionRepository;
import site.hospital.repository.review.ReviewRepository;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final StaffHosRepository staffHosRepository;
    private final HospitalSearchRepository hospitalSearchRepository;
    private final HospitalViewRepository hospitalViewRepository;
    private final AdminHospitalSearchRepository adminHospitalSearchRepository;
    private final DetailedHosRepository detailedHosRepository;
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;
    private final EstimationRepository estimationRepository;


    //병원 + 상세 정보등록
    @Transactional
    public Long register(Hospital hospital, DetailedHosInformation detailedHosInformation){

        hospital.changeDetailedHosInformation(detailedHosInformation);
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    //병원만 등록.
    @Transactional
    public Long registerHospital(Hospital hospital){
        hospitalRepository.save(hospital);

        return hospital.getId();
    }

    //병원 조회
    public Hospital findOne(Long hospitalId){
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);
        return hospital;
    }

    //병원 검색
    public Page<HospitalSearchDto> searchHospital(String searchName,Pageable pageable){
        return hospitalSearchRepository.searchHospital(searchName,pageable);
    }


    //관리자 병원 추가정보 + 사진 등록 +의사 등록
    @Transactional
    public Long adminRegisterStaffHosInformation(Long hospitalId, StaffHosInformation staffHosInformation,
                                                 List<Doctor> doctors){
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        //병원 테이블 추가정보 유무 확인
        if(hospital.getStaffHosInformation() !=null) throw new IllegalStateException("이미 추가 정보가 있습니다.");

        staffHosInformation.createStaffHosInformation(staffHosInformation, doctors);
        staffHosRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);
        hospitalRepository.save(hospital);

        return staffHosInformation.getId();
    }

    //관리자 병원 추가정보만 등록.
    @Transactional
    public Long adminRegisterStaffHosInfo(Long hospitalId, StaffHosInformation staffHosInformation){
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));

        staffHosRepository.save(staffHosInformation);

        //양방향 연관관계
        hospital.changeStaffHosInformation(staffHosInformation);
        hospitalRepository.save(hospital);

        return staffHosInformation.getId();
    }



    //병원 + 상세 정보 수정
    @Transactional
    public Long modifyAllHosInformation(Long hospitalId, Long detailedHosId,
                                        ModifyHospitalRequest request){
        //병원 정보만 수정.
        Hospital selectHospital = hospitalRepository.findById(hospitalId).orElse(null);

        if(detailedHosId == null) {

            selectHospital.updateHospital(request.getLicensingDate(), request.getHospitalName(), request.getPhoneNumber(),
                    request.getDistinguishedName(),
                    request.getMedicalSubjectInformation(), request.getBusinessCondition(),
                    request.getCityName());
        }
        //병원 + 상세 정보 수정.
        else {
            DetailedHosInformation selectDetailedHosInfo = detailedHosRepository.findById(detailedHosId).orElse(null);

            selectHospital.updateHospital(request.getLicensingDate(), request.getHospitalName(), request.getPhoneNumber(),
                        request.getDistinguishedName(),
                    request.getMedicalSubjectInformation(), request.getBusinessCondition(),
                    request.getCityName());

            selectDetailedHosInfo.updateDetailedHosInformation(request.getNumberWard(),
                    request.getNumberHealthcareProvider(),request.getNumberPatientRoom(),
                    request.getHospitalAddress(),request.getHospitalLocation());
        }
        return selectHospital.getId();
    }

    //병원 정보 상세 보기
    public ViewHospitalDTO viewHospital(Long hospitalId){
        return hospitalViewRepository.viewHospital(hospitalId);
    }

    //관리자 병원 검색
    public Page<AdminSearchHospitalDto> adminSearchHospitals(AdminHospitalSearchCondition condition, Pageable pageable){
        return adminHospitalSearchRepository.adminSearchHospitals(condition, pageable);
    }

    //관리자 병원 보기
    public Hospital adminViewHospital(Long hospitalId){
        return hospitalRepository.adminViewHospital(hospitalId);
    }

    //관리자 병원 삭제하기
    @Transactional
    public void adminDeleteHospital(Long hospitalId, Long staffHosId){
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        //병원 STAFF 정보도 삭제.
        if(staffHosId!=null)
        {
           if(hospital.getStaffHosInformation().getId() != staffHosId)
               throw new IllegalStateException("해당 병원과 staffId가 일치하지 않습니다.");

           StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId).orElse(null);
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
    public void adminUpdateHospital(Long hospitalId, AdminModifyHospitalRequest request){
        Hospital modifyHospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원 정보가 존재하지 않습니다."));
        Hospital hospital = Hospital.builder().hospitalName(request.getHospitalName())
                .cityName(request.getCityName()).businessCondition(request.getBusinessCondition())
                .medicalSubjectInformation(request.getMedicalSubjectInformation())
                .distinguishedName(request.getDistinguishedName())
                .phoneNumber(request.getPhoneNumber()).licensingDate(request.getLicensingDate()).build();

        modifyHospital.modifyHospital(hospital);

        //병원 추가정보 수정 유무
        if(request.getDetailedModifyCheck()==true)
        {
            //detailed hospitalId가 일치하지 않으면 수정 취소.
            if(modifyHospital.getDetailedHosInformation().getId() != request.getDetailedHosInfoId())
                throw new IllegalStateException("DetailedHosInfoId가 일치하지 않습니다.");

            DetailedHosInformation detailedHosInformation =detailedHosRepository.findById(request.getDetailedHosInfoId())
                    .orElseThrow(()->new IllegalStateException("해당 id에 속하는 상세 병원 정보가 존재하지 않습니다."));

            DetailedHosInformation modifyDetailedHosInformation = DetailedHosInformation.builder()
                    .numberPatientRoom(request.getNumberPatientRoom()).numberWard(request.getNumberWard())
                    .numberHealthcareProvider(request.getNumberHealthcareProvider()).hospitalAddress(request.getHospitalAddress())
                    .hospitalLocation(request.getHospitalLocation()).build();

            detailedHosInformation.modifyDetailedHosInformation(modifyDetailedHosInformation);
        }
    }


}
