package site.hospital.hospital.user.api;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.hospital.user.repository.searchQuery.HospitalSearchDto;
import site.hospital.hospital.user.repository.viewQuery.ViewHospitalDTO;
import site.hospital.hospital.user.service.HospitalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HospitalController {

    private final HospitalService hospitalService;

    //병원 전체 검색
    @GetMapping("/search/hospital/{searchName}")
    public Page<HospitalSearchDto> searchHospital(
            @PathVariable("searchName") String searchName,
            Pageable pageable
    ) {
        return hospitalService.searchHospital(searchName, pageable);
    }

    //병원 정보 보기(고객)
    @GetMapping("/hospital/view/{hospitalId}")
    public ViewHospitalDTO viewsHospital(@PathVariable("hospitalId") Long hospitalId) {
        return hospitalService.viewHospital(hospitalId);
    }

}
