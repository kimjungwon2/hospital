package site.hospital.repository.postTag;

import java.util.List;
import site.hospital.domain.PostTag;

public interface PostTagRepositoryCustom {

    List<PostTag> listPostTag(Long hospitalId);
}
