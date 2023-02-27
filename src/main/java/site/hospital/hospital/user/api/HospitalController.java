package site.hospital.hospital.user.api;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.hospital.user.repository.search.HospitalSearchSelectQuery;
import site.hospital.hospital.user.repository.view.HospitalViewSelectQuery;
import site.hospital.hospital.user.service.HospitalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/search/hospital/{searchName}")
    public Page<HospitalSearchSelectQuery> searchHospital(
            @PathVariable("searchName") String searchName,
            Pageable pageable
    ) {
        return hospitalService.searchHospital(searchName, pageable);
    }


    @GetMapping("/hospital/view/{hospitalId}")
    public HospitalViewSelectQuery viewHospital(@PathVariable("hospitalId") Long hospitalId) {
        return hospitalService.viewHospital(hospitalId);
    }
}
