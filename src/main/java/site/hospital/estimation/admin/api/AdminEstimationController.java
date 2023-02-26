package site.hospital.estimation.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.estimation.admin.service.AdminEstimationService;
import site.hospital.estimation.admin.api.dto.EstimationAdminModifyRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminEstimationController {

    private final AdminEstimationService adminEstimationService;

    @PostMapping("/admin/estimation/register")
    public EstimationCreateResponse adminCreateEstimation(
            @RequestBody @Validated EstimationCreateRequest request) {
        return adminEstimationService.registerHospitalEstimation(request);
    }

    @DeleteMapping("/admin/estimation/delete/{estimationId}")
    public void adminDeleteEstimation(@PathVariable("estimationId") Long estimationId) {
        adminEstimationService.deleteEstimation(estimationId);
    }

    @PutMapping("/admin/estimation/modify/{estimationId}")
    public void adminModifyMember(
            @PathVariable("estimationId") Long estimationId,
            @RequestBody @Validated EstimationAdminModifyRequest request
    ) {
        adminEstimationService.modifyEstimation(estimationId, request);
    }

}
