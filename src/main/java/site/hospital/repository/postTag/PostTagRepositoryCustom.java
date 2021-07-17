package site.hospital.repository.postTag;

import site.hospital.domain.PostTag;

import java.util.List;

public interface PostTagRepositoryCustom {
    List<PostTag> listPostTag(Long hospitalId);
}
