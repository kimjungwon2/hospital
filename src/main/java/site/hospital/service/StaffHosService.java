package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.domain.Hospital;
import site.hospital.domain.HospitalImage;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.Doctor;
import site.hospital.dto.ModifyStaffHospitalRequest;
import site.hospital.dto.staffHosInfo.AdminModifyStaffHosRequest;
import site.hospital.repository.HospitalImageRepository;
import site.hospital.repository.StaffHosRepository;
import site.hospital.repository.hospital.HospitalRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffHosService {
    private final StaffHosRepository staffHosRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalImageService hospitalImageService;
    private final HospitalImageRepository hospitalImageRepository;

    //병원 추가 정보 보기(고객)
    public StaffHosInformation viewStaffHosInfo(Long staffHosId){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));

        return staffHosInformation;
    }

    //병원 추가 정보 삭제
    @Transactional
    public void adminDeleteStaffHosInfo(Long staffHosId){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findByStaffHosId(staffHosId);
        hospital.deleteStaffHosId();

        staffHosRepository.deleteById(staffHosId);
    }

    @Transactional
    public void adminModifyStaffHosInfo(Long staffHosId, AdminModifyStaffHosRequest request){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 직원이 추가하는 병원 정보가 존재하지 않습니다."));
        StaffHosInformation modifyStaffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality()).consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).build();

        staffHosInformation.modifyStaffHosInformation(modifyStaffHosInformation);

    }

    @Transactional
    public void addHospitalPhoto(Long staffHosInfoId, List<MultipartFile> files) throws Exception{
        List<HospitalImage> hospitalImages = hospitalImageService.fileInfo(staffHosInfoId,files);

        //파일이 비어있으면 오류.
        if(hospitalImages.isEmpty()){
            throw new IllegalStateException("사진을 등록하세요");
        }
        else{
            List<HospitalImage> hospitalBean = new ArrayList<>();
            for(HospitalImage hospitalImage:hospitalImages){
                hospitalImageRepository.save(hospitalImage);
            }
        }
    }
}
