package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.service.DetailedHosService;

@RestController
@RequiredArgsConstructor
public class DetailedHosApiController {

    private final DetailedHosService detailedHosService;

}
