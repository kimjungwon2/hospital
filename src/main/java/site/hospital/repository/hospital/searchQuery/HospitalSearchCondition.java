package site.hospital.repository.hospital.searchQuery;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class HospitalSearchCondition {
    @NotBlank(message="아무것도 검색을 하지 않았거나, 공백으로만 검색하셨습니다.")
    private String searchName;
}
