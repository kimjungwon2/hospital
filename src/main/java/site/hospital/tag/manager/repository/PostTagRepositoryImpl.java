package site.hospital.tag.manager.repository;

import static site.hospital.tag.manager.domain.QPostTag.postTag;
import static site.hospital.tag.manager.domain.QTag.tag;
import static site.hospital.hospital.user.domain.QHospital.hospital;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import site.hospital.tag.manager.domain.PostTag;


public class PostTagRepositoryImpl implements PostTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostTagRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PostTag> viewPostTags(Long hospitalId) {
        return queryFactory
                .select(postTag)
                .from(postTag)
                .join(postTag.tag, tag).fetchJoin()
                .join(postTag.hospital, hospital).fetchJoin()
                .where(hospitalIdEq(hospitalId))
                .fetch();
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id == null ? null : postTag.hospital.id.eq(id);
    }

}
