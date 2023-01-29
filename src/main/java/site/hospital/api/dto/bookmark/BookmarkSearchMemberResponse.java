package site.hospital.api.dto.bookmark;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.Bookmark;
import site.hospital.domain.hospital.BusinessCondition;

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
