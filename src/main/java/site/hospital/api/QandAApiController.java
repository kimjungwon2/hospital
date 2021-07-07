package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.service.QandAService;

@RestController
@RequiredArgsConstructor
public class QandAApiController {
    private final QandAService qandAService;
}
