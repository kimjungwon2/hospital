package site.hospital.hospital.admin.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.hospital.admin.repository.dto.view.AdminHospitalView;
import site.hospital.hospital.admin.repository.dto.AdminModifyHospitalRequest;
import site.hospital.hospital.admin.repository.search.AdminHospitalSearchSelectQuery;
import site.hospital.hospital.user.api.dto.HospitalCreateDetailedHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateRequest;
import site.hospital.hospital.user.api.dto.HospitalCreateStaffHosInfoRequest;
import site.hospital.hospital.user.api.dto.HospitalResponse;
import site.hospital.hospital.user.domain.BusinessCondition;


public interface AdminHospitalService {


    Page<AdminHospitalSearchSelectQuery> searchHospitals(
            Long hospitalId,
            String hospitalName,
            BusinessCondition businessCondition,
            String cityName,
            Pageable pageable
    );

    AdminHospitalView viewHospital(
            Long hospitalId,
            Long detailedHosInfoId,
            Long staffHosInfoId,
            Long thumbnailId
    );

    void deleteHospital(Long hospitalId, Long hosAdditionalInfoId);

    HospitalResponse modifyHospital(Long hospitalId, AdminModifyHospitalRequest request);

    HospitalResponse registerHospital(HospitalCreateRequest request);

    HospitalResponse registerHosAdditionalInfo(HospitalCreateStaffHosInfoRequest request);
    void deleteDetailedHospitalInfo(Long detailedHosInfoId);

    HospitalResponse registerDetailedHosInfo(
            HospitalCreateDetailedHosInfoRequest request
            , Long hospitalId
    );
}
