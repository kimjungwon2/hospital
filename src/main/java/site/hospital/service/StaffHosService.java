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

    //병원 추가 정보 보기(고객)
    public StaffHosInformation viewStaffHosInfo(Long staffHosId){
        StaffHosInformation staffHosInformation = staffHosRepository.findById(staffHosId).orElse(null);

        return staffHosInformation;
    }

}
