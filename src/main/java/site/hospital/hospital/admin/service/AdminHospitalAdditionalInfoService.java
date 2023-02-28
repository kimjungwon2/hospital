package site.hospital.hospital.admin.service;

import site.hospital.hospital.admin.repository.dto.hospitalAdditionalInfo.AdminModifyStaffHosRequest;


public interface AdminHospitalAdditionalInfoService {

    void deleteHospitalAdditionalInfo(Long hosAdditionalInfoId);

    void modifyHospitalAdditionalInfo(
            Long hosAdditionalInfoId,
            AdminModifyStaffHosRequest request
    );

}
