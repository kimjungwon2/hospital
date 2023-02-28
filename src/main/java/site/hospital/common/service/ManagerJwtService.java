package site.hospital.common.service;

import javax.servlet.ServletRequest;

public interface ManagerJwtService {


    void accessManager(
            ServletRequest servletRequest,
            Long hospitalIdRequest
    );

    Long getHospitalNumber(ServletRequest servletRequest);

}
