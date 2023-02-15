package site.hospital.review.user.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import site.hospital.review.user.domain.reviewHospital.Recommendation;

@Data
public class ReviewCreateRequest {

    @NotNull(message = "멤버 번호를 입력해주세요.")
    private Long memberId;
    @NotNull(message = "병원 번호를 입력해주세요.")
    private Long hospitalId;
    @NotNull(message = "질문 내용을 입력해주세요.")
    private String content;
    @NotNull(message = "진료 병명을 입력해주세요.")
    private String disease;
    @NotNull(message = "추천 여부를 선택해주세요.")
    private Recommendation recommendationStatus;
    @NotNull(message = "가격 점수를 입력해주세요.")
    private Integer sumPrice;
    @NotNull(message = "친절함 점수를 입력해주세요.")
    private Integer kindness;
    @NotNull(message = "증상 완화 점수를 입력해주세요.")
    private Integer symptomRelief;
    @NotNull(message = "청결을 점수를 입력해주세요.")
    private Integer cleanliness;
    @NotNull(message = "대기 시간 점수를 입력해주세요.")
    private Integer waitTime;

}
