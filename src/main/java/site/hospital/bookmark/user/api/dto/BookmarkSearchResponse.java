package site.hospital.bookmark.user.api.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.hospital.user.domain.BusinessCondition;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class BookmarkSearchResponse {

    private final Long bookmarkId;
    private final Long hospitalId;
    private final Long memberId;
    private final String medicalSubjectInformation;
    private final BusinessCondition businessCondition;
    private final String cityName;
    private final String userName;
    private final String hospitalName;
    private final LocalDateTime createTime;

    public static BookmarkSearchResponse from(Bookmark bookmark) {
        Assert.notNull(bookmark.getHospital(),"hospital must be provided");
        Assert.notNull(bookmark.getMember(),"member must be provided");

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
