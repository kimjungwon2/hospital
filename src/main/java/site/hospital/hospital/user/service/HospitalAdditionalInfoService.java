package site.hospital.hospital.user.service;

import site.hospital.hospital.user.api.dto.additionalinfo.HospitalAdditionalInfoViewResponse;


public interface HospitalAdditionalInfoService {
    HospitalAdditionalInfoViewResponse viewHospitalAdditionalInfo(Long hosAdditionalInfoId);
}
