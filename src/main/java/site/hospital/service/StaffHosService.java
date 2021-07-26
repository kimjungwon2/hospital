package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.StaffHosInformation;
import site.hospital.domain.Doctor;
import site.hospital.dto.ModifyStaffHospitalRequest;
import site.hospital.repository.StaffHosRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffHosService {
    private final StaffHosRepository staffHosRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;

    //병원 추가 정보 보기(고객)
    public StaffHosInformation viewStaffHosInfo(Long staffHosId){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId).orElse(null);

        return staffHosInformation;
    }

    //병원 추가 정보 삭제
    @Transactional
    public void adminDeleteStaffHosInfo(Long staffHosId){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId).orElse(null);
        Hospital hospital = hospitalRepository.findByStaffHosId(staffHosId);
        hospital.deleteStaffHosId();

        staffHosRepository.deleteById(staffHosId);
    }

    @Transactional
    public void adminModifyStaffHosInfo(Long staffHosId, AdminModifyStaffHosRequest request){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId).orElse(null);
        StaffHosInformation modifyStaffHosInformation = StaffHosInformation.builder()
                .abnormality(request.getAbnormality()).consultationHour(request.getConsultationHour())
                .introduction(request.getIntroduction()).photo(request.getPhoto()).build();

        staffHosInformation.modifyStaffHosInformation(modifyStaffHosInformation);

    }
}
