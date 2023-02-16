package site.hospital.review.user.repository;


import static site.hospital.domain.QReviewImage.reviewImage;
import static site.hospital.domain.member.QMember.member;
import static site.hospital.domain.review.QReview.review;
import static site.hospital.domain.reviewHospital.QReviewHospital.reviewHospital;

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
import site.hospital.review.user.repository.dto.StaffReviewSearchCondition;


public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> hospitalReviewSearch(Long hospitalId, Long memberId) {
        List<Review> result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .where(hospitalIdEq(hospitalId), memberIdEq(memberId))
                .fetch();

        return result;
    }

    @Override
    public Review viewHospitalReview(Long reviewId) {
        Review result = queryFactory
                .select(review)
                .from(review)
                .join(review.member, member).fetchJoin()
                .leftJoin(review.reviewImage, reviewImage).fetchJoin()
                .where(reviewIdEq(reviewId))
                .fetchOne();

        return result;
    }

    @Override
    public Page<Review> staffSearchReviews(Long hospitalId, StaffReviewSearchCondition condition,
            Pageable pageable) {
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
        //리뷰 아이디
        List<Long> reviewIds = queryFactory
                .select(reviewHospital.review.id)
                .from(reviewHospital)
                .where(reviewHospital.hospital.eq(hospital))
                .fetch();

        //리뷰 hospital 먼저 삭제
        queryFactory.delete(reviewHospital)
                .where(reviewHospital.hospital.eq(hospital))
                .execute();

        //리뷰 삭제.
        queryFactory.delete(review)
                .where(review.id.in(reviewIds))
                .execute();
    }

    @Override
    public Page<Review> adminSearchReviews(AdminReviewSearchCondition condition,
            Pageable pageable) {
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

    //미승인 리뷰의 갯수
    @Override
    public Long adminUnapprovedReviewCount() {
        return queryFactory.select(review)
                .from(review)
                .where(review.authenticationStatus.eq(ReviewAuthentication.WAITING))
                .fetchCount();
    }

    //미승인 리뷰만 보이기
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
