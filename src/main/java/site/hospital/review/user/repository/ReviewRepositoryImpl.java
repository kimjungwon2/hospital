package site.hospital.review.user.repository;


import static site.hospital.review.user.domain.QReviewImage.reviewImage;
import static site.hospital.member.user.domain.QMember.member;
import static site.hospital.review.user.domain.QReview.review;
import static site.hospital.review.user.domain.reviewhospital.QReviewHospital.reviewHospital;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;
import site.hospital.review.admin.repository.dto.AdminReviewSearchCondition;
import site.hospital.review.manager.repository.dto.ManagerReviewSearchCondition;


public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> searchHospitalReviews(Long hospitalId, Long memberId) {
        return queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(hospitalIdEq(hospitalId), memberIdEq(memberId))
                .fetch();
    }

    @Override
    public Review viewHospitalReview(Long reviewId) {
        return queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .leftJoin(review.reviewImage, reviewImage).fetchJoin()
                .where(reviewIdEq(reviewId))
                .fetchOne();

    }

    @Override
    public Page<Review> managerSearchReviews(
            Long hospitalId,
            ManagerReviewSearchCondition condition,
            Pageable pageable
    ) {
        QueryResults<Review> result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(
                        hospitalIdEq(hospitalId),
                        nickNameEq(condition.getNickName()),
                        memberIdNameLike(condition.getMemberIdName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Review> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public void adminDeleteReviewHospital(Hospital hospital) {
        List<Long> reviewIds =
                queryFactory
                .select(reviewHospital.review.id)
                .from(reviewHospital)
                .where(reviewHospital.hospital.eq(hospital))
                .fetch();

        deleteReviewHospital(hospital);
        deleteReviews(reviewIds);
    }

    @Override
    public Page<Review> adminSearchReviews(
            AdminReviewSearchCondition condition,
            Pageable pageable
    ) {
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

    @Override
    public Long adminCountUnapprovedReview() {
        return queryFactory.select(review)
                .from(review)
                .where(review.authenticationStatus.eq(ReviewAuthentication.WAITING))
                .fetchCount();
    }

    @Override
    public Page<Review> adminSearchUnapprovedReviews(Pageable pageable) {
        QueryResults<Review> result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(
                        review.authenticationStatus.eq(ReviewAuthentication.WAITING))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Review> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private void deleteReviews(List<Long> reviewIds) {
        queryFactory
                .delete(review)
                .where(review.id.in(reviewIds))
                .execute();
    }

    private void deleteReviewHospital(Hospital hospital) {
        queryFactory
                .delete(reviewHospital)
                .where(reviewHospital.hospital.eq(hospital))
                .execute();
    }

    private BooleanExpression memberIdEq(Long id) {
        return id == null ? null : review.member.id.eq(id);
    }

    private BooleanExpression reviewIdEq(Long id) {
        return id == null ? null : review.id.eq(id);
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id == null ? null : review.reviewHospitals.any().hospital.id.eq(id);
    }

    private BooleanExpression nickNameEq(String nickName) {
        return nickName == null ? null : member.nickName.eq(nickName);
    }

    private BooleanExpression hospitalNameLike(String hospitalName) {
        return hospitalName == null ? null
                : review.reviewHospitals.any().hospital.hospitalName.contains(hospitalName);
    }

    private BooleanExpression memberIdNameLike(String memberIdName) {
        return memberIdName == null ? null : member.memberIdName.contains(memberIdName);
    }
}
