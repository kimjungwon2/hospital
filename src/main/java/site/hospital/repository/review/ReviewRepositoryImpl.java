package site.hospital.repository.review;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.review.Review;
import site.hospital.dto.AdminReviewSearchCondition;

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
    public List<Review> hospitalReviewSearch(Long hospitalId, Long memberId){
        List<Review> result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(hospitalIdEq(hospitalId),memberIdEq(memberId))
                .fetch();

        return result;
    }

    @Override
    public Review viewHospitalReview(Long reviewId){
        Review result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(reviewIdEq(reviewId))
                .fetchOne();

        return result;
    }

    @Override
    public Page<Review> adminSearchReviews(AdminReviewSearchCondition condition, Pageable pageable){
        QueryResults<Review> result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(
                        nickNameEq(condition.getNickName()),
                        hospitalNameLike(condition.getHospitalName()),
                        memberIdNameLike(condition.getMemberIdName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Review> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
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
    private BooleanExpression nickNameEq(String nickName){ return nickName == null? null: review.member.nickName.eq(nickName);}
    private BooleanExpression hospitalNameLike(String hospitalName){ return hospitalName == null? null: review.reviewHospitals.any().hospital.hospitalName.contains(hospitalName);}
    private BooleanExpression memberIdNameLike(String memberIdName){ return memberIdName == null? null: review.member.memberIdName.contains(memberIdName);}
}
