package site.hospital.review.user.repository.query;

import static org.springframework.util.ObjectUtils.isEmpty;
import static site.hospital.review.user.domain.QReviewLike.reviewLike;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.member.user.domain.QMember.member;
import static site.hospital.review.user.domain.QReview.review;
import static site.hospital.review.user.domain.reviewHospital.QReviewHospital.reviewHospital;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import site.hospital.review.user.domain.Review;

@Repository
public class ReviewSearchRepository {


    private final JPAQueryFactory queryFactory;

    public ReviewSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<ReviewSearchDto> searchReview(String searchName, Pageable pageable) {
        Page<ReviewSearchDto> result = findReviews(searchName, pageable);

        //비어있으면 바로 반환.
        if (result.getContent().isEmpty()) {
            return result;
        }

        List<Long> reviewIds = result.stream().map(r -> r.getReviewId())
                .collect(Collectors.toList());

        List<ReviewHospitalDTO2> reviewHospitalDto =
                queryFactory
                        .select(new QReviewHospitalDTO2(review.id
                                , reviewHospital.content, reviewHospital.disease,
                                reviewHospital.evCriteria.averageRate,
                                hospital.id
                                , hospital.hospitalName))
                        .from(reviewHospital)
                        .join(reviewHospital.review, review)
                        .join(reviewHospital.hospital, hospital)
                        .where(reviewHospital.review.id.in(reviewIds))
                        .fetch();

        Map<Long, List<ReviewHospitalDTO2>> reviewHospitalMap = reviewHospitalDto.stream()
                .collect(Collectors
                        .groupingBy(ReviewHospitalDTO -> ReviewHospitalDTO.getReviewId()));

        result.forEach(r -> r.setReviewHospitals(reviewHospitalMap.get(r.getReviewId())));

        List<ReviewLikeSearchDTO> reviewLikeSearchDTO =
                queryFactory
                        .select(new QReviewLikeSearchDTO(reviewLike.id, review.id))
                        .from(reviewLike)
                        .join(reviewLike.review, review)
                        .where(reviewLike.review.id.in(reviewIds))
                        .fetch();

        Map<Long, List<ReviewLikeSearchDTO>> reviewLikeMap = reviewLikeSearchDTO.stream()
                .collect(Collectors
                        .groupingBy(ReviewLikeSearchDTO -> ReviewLikeSearchDTO.getReviewId()));

        result.forEach(r -> r.setReviewLikes(reviewLikeMap.get(r.getReviewId())));

        return result;
    }

    private Page<ReviewSearchDto> findReviews(String searchName, Pageable pageable) {
        List<ReviewSearchDto> content = queryFactory
                .select(new QReviewSearchDto(review.id, member.nickName, review.createdDate,
                        review.authenticationStatus))
                .from(review)
                .join(review.member, member)
                .where(
                        reviewHospitalContent(searchName)
                                .or(reviewHospitalDisease(searchName))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Review> countQuery = queryFactory
                .selectFrom(review)
                .join(review.member, member)
                .where(
                        reviewHospitalContent(searchName)
                                .or(reviewHospitalDisease(searchName))
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    private BooleanExpression reviewHospitalContent(String name) {
        return isEmpty(name) ? null : review.reviewHospitals.any().content.contains(name);
    }

    private BooleanExpression reviewHospitalDisease(String name) {
        return isEmpty(name) ? null : review.reviewHospitals.any().disease.contains(name);
    }

}