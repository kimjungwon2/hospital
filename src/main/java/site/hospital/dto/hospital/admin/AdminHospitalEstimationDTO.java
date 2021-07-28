package site.hospital.dto.hospital.admin;

import site.hospital.domain.Estimation;

public class AdminHospitalEstimationDTO {
    private Long estimationId;

    private String city;
    private String hospitalName;
    private String distinctionGrade;
    private String estimationList;

    public AdminHospitalEstimationDTO(Estimation estimation) {
        this.estimationId = estimation.getId();
        this.city = estimation.getCityName();
        this.hospitalName = estimation.getHospitalName();
        this.distinctionGrade = estimation.getDistinctionGrade();
        this.estimationList = estimation.getEstimationList();
    }
}
