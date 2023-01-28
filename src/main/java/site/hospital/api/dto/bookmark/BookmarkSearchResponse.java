package site.hospital.api.dto.bookmark;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.Bookmark;
import site.hospital.domain.hospital.BusinessCondition;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class BookmarkSearchResponse {

    private Long bookmarkId;
    private Long hospitalId;
    private Long memberId;
    private String medicalSubjectInformation;
    private BusinessCondition businessCondition;
    private String cityName;
    private String userName;
    private String hospitalName;
    private LocalDateTime createTime;

    public static BookmarkSearchResponse from(Bookmark bookmark) {
        return BookmarkSearchResponse
                .builder()
                .bookmarkId(bookmark.getId())
                .hospitalId(bookmark.getHospital().getId())
                .memberId(bookmark.getMember().getId())
                .medicalSubjectInformation(bookmark.getHospital().getMedicalSubjectInformation())
                .businessCondition(bookmark.getHospital().getBusinessCondition())
                .cityName(bookmark.getHospital().getCityName())
                .userName(bookmark.getMember().getUserName())
                .hospitalName(bookmark.getHospital().getHospitalName())
                .createTime(bookmark.getCreatedDate())
                .build();
    }
}
