package site.hospital.tag.repository;

import java.util.List;
import site.hospital.tag.domain.PostTag;

public interface PostTagRepositoryCustom {

    List<PostTag> listPostTag(Long hospitalId);
}
