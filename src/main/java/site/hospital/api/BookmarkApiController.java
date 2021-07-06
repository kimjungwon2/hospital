package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.service.BookmarkService;

@RestController
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    @PostMapping("/hospital/bookmark")
    public CreateBookmarkResponse saveBookmark(@RequestBody @Validated CreateBookmarkRequest request){
        Long id = bookmarkService.bookmark(request.getMemberId(), request.hospitalId);

        return new CreateBookmarkResponse(id);
    }


    /* DTO */
    @Data
    private static class CreateBookmarkResponse{
        long id;
        public CreateBookmarkResponse(long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateBookmarkRequest{
        long memberId;
        long hospitalId;
    }
}
