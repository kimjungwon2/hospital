package site.hospital.repository.review;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.review.Review;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.review.QReview.review;
import static site.hospital.domain.member.QMember.member;
import static site.hospital.domain.reviewHospital.QReviewHospital.reviewHospital;


public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> hospitalReviewSearch(Long hospitalId, Long reviewId){
        List<Review> result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(hospitalIdEq(hospitalId),reviewIdEq(reviewId))
                .fetch();

        return result;
    }

    private BooleanExpression reviewIdEq(Long id){
        return id != null? review.id.eq(id) :null;
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id != null? review.reviewHospitals.any().hospital.id.eq(id) :null;
    }

}
