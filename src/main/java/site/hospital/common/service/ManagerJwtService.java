package site.hospital.common.service;

import javax.servlet.ServletRequest;

public interface ManagerJwtService {


    void accessManager(
            ServletRequest servletRequest,
            Long memberId,
            Long existingHospitalId
    );

    Long getHospitalNumber(ServletRequest servletRequest);

}
