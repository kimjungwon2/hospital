package site.hospital.hospital.manager.service;

import java.util.List;
import javax.servlet.ServletRequest;
import site.hospital.hospital.manager.api.dto.view.ManagerHospitalViewResponse;
import site.hospital.hospital.manager.api.dto.ManagerModifyHospitalRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateDetailHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalManagerCreateHosAdditionalInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.api.dto.HospitalViewImageResponse;
import site.hospital.hospital.user.domain.HospitalThumbnail;

public interface ManagerHospitalService {


    List<HospitalViewImageResponse> viewHospitalImages(Long hospitalId);

    ManagerHospitalViewResponse viewHospital(ServletRequest servletRequest);

    HospitalResponse modifyHospital(
            ServletRequest servletRequest,
            Long hospitalId,
            ManagerModifyHospitalRequest request
    );

    HospitalResponse registerHosAdditionalInfo(
            ServletRequest servletRequest,
            HospitalManagerCreateHosAdditionalInfoRequest request
    );

    HospitalResponse registerDetailedHosInfo(
            ServletRequest servletRequest,
            HospitalManagerCreateDetailHosInfoRequest request
    );

    void deleteDetailedHospitalInfo(
            ServletRequest servletRequest,
            Long memberId,
            Long detailedHosInfoId
    );

    HospitalThumbnail viewThumbnail(Long thumbnailId);

}
