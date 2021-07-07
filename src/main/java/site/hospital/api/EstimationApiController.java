package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.service.EstimationService;

@RestController
@RequiredArgsConstructor
public class EstimationApiController {

    private final EstimationService estimationService;
}
