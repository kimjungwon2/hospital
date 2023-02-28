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
public class BookmarkSearchMemberResponse {

    private final Long hospitalId;
    private final String medicalSubjectInformation;
    private final BusinessCondition businessCondition;
    private final String cityName;
    private final String hospitalName;
    private final LocalDateTime createTime;

    public static BookmarkSearchMemberResponse from(Bookmark bookmark) {
        Assert.notNull(bookmark.getHospital(),"hospital must be provided");

        return BookmarkSearchMemberResponse
                .builder()
                .hospitalId(bookmark.getHospital().getId())
                .medicalSubjectInformation(bookmark.getHospital().getMedicalSubjectInformation())
                .businessCondition(bookmark.getHospital().getBusinessCondition())
                .cityName(bookmark.getHospital().getCityName())
                .hospitalName(bookmark.getHospital().getHospitalName())
                .createTime(bookmark.getCreatedDate())
                .build();
    }

}
