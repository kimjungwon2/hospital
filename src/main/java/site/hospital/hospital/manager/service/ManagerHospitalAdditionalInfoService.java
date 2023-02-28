package site.hospital.hospital.manager.service;

import javax.servlet.ServletRequest;
import site.hospital.hospital.manager.api.dto.StaffModifyStaffHosRequest;


public interface ManagerHospitalAdditionalInfoService {

    void modifyHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long hosAdditionalInfoId,
            StaffModifyStaffHosRequest request
    );

    void deleteHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long memberId,
            Long hosAdditionalInfoId
    );

}
