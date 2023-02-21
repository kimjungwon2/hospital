package site.hospital.tag.manager.repository;

import java.util.List;
import site.hospital.tag.manager.domain.PostTag;

public interface PostTagRepositoryCustom {

    List<PostTag> viewPostTags(Long hospitalId);
}
