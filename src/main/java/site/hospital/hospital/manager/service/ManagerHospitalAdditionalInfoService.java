package site.hospital.hospital.manager.service;

import javax.servlet.ServletRequest;
import site.hospital.hospital.manager.api.dto.ManagerModifyStaffHosRequest;


public interface ManagerHospitalAdditionalInfoService {

    void modifyHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long hosAdditionalInfoId,
            ManagerModifyStaffHosRequest request
    );

    void deleteHospitalAdditionalInfo(
            ServletRequest servletRequest,
            Long memberId,
            Long hosAdditionalInfoId
    );

}
