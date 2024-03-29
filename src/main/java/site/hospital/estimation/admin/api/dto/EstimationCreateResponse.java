package site.hospital.estimation.admin.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class EstimationCreateResponse {

    private final long estimationId;

    public static EstimationCreateResponse from(long estimationId) {
        return EstimationCreateResponse
                .builder()
                .estimationId(estimationId)
                .build();
    }

}
