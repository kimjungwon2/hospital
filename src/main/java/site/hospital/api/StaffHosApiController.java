package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.service.StaffHosService;

@RestController
@RequiredArgsConstructor
public class StaffHosApiController {

    private final StaffHosService staffHosService;

}
