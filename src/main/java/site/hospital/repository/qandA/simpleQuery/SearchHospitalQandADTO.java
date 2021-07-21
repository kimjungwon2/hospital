package site.hospital.repository.qandA.simpleQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SearchHospitalQandADTO {
    private Long qandAId;
    private String nickName;
    private String content;
    private Long answerId;
    private String answerContent;

    @QueryProjection
    public SearchHospitalQandADTO(Long qandAId, String nickName, String content,
                                  Long answerId, String answerContent) {
        this.qandAId = qandAId;
        this.nickName = nickName;
        this.content = content;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }
}
