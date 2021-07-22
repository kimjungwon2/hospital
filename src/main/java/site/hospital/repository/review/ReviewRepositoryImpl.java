package site.hospital.repository.review;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.review.Review;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.review.QReview.review;
import static site.hospital.domain.member.QMember.member;


public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> hospitalReviewSearch(Long hospitalId, Long memberId, Long reviewId){
        List<Review> result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(hospitalIdEq(hospitalId),reviewIdEq(reviewId),memberIdEq(memberId))
                .fetch();

        return result;
    }



    private BooleanExpression memberIdEq(Long id){
        return id == null? null:review.member.id.eq(id);
    }
    private BooleanExpression reviewIdEq(Long id){
        return id == null? null: review.id.eq(id);
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id == null? null: review.reviewHospitals.any().hospital.id.eq(id);
    }

}
