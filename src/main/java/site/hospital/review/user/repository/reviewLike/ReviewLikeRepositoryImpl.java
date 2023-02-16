package site.hospital.review.user.repository.reviewLike;

import static site.hospital.review.user.domain.QReviewLike.reviewLike;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import site.hospital.review.user.domain.ReviewLike;

public class ReviewLikeRepositoryImpl implements ReviewLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewLikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    //고객의 즐겨찾기 유무 확인
    public ReviewLike isLikeReview(Long memberId, Long reviewId) {
        ReviewLike result = queryFactory
                .select(reviewLike)
                .from(reviewLike)
                .where(memberIdEq(memberId), reviewIdEq(reviewId))
                .fetchOne();

        return result;
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? reviewLike.member.id.eq(id) : null;
    }

    private BooleanExpression reviewIdEq(Long id) {
        return id != null ? reviewLike.review.id.eq(id) : null;
    }

}
