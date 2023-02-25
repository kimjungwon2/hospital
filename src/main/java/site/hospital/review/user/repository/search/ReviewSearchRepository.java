package site.hospital.review.user.repository.search;

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

    public Page<ReviewSearchSelectQuery> searchReviews(String searchWord, Pageable pageable) {
        Page<ReviewSearchSelectQuery> searchResults = findReviews(searchWord, pageable);

        if (checkEmpty(searchResults)) {
            return searchResults;
        }

        List<Long> reviewIds =
                searchResults
                .stream()
                .map(r -> r.getReviewId())
                .collect(Collectors.toList());

        insertReviewHospital(searchResults, reviewIds);
        insertReviewLike(searchResults, reviewIds);

        return searchResults;
    }

    private Page<ReviewSearchSelectQuery> findReviews(String searchWord, Pageable pageable) {
        List<ReviewSearchSelectQuery> content = queryFactory
                .select(new QReviewSearchSelectQuery(
                        review.id,
                        member.nickName,
                        review.createdDate,
                        review.authenticationStatus))
                .from(review)
                .join(review.member, member)
                .where(
                        reviewHospitalContent(searchWord)
                                .or(reviewHospitalDisease(searchWord))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Review> countQuery = getTotalCount(searchWord);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    private void insertReviewLike(Page<ReviewSearchSelectQuery> result, List<Long> reviewIds) {
        List<ReviewSearchLikeDTO> reviewSearchLikeDTO =
                queryFactory
                        .select(new QReviewSearchLikeDTO(reviewLike.id, review.id))
                        .from(reviewLike)
                        .join(reviewLike.review, review)
                        .where(reviewLike.review.id.in(reviewIds))
                        .fetch();

        Map<Long, List<ReviewSearchLikeDTO>> reviewLikeMap = 
                reviewSearchLikeDTO
                .stream()
                .collect(Collectors
                .groupingBy(ReviewSearchLikeDTO -> ReviewSearchLikeDTO.getReviewId()));

        result.forEach(r -> r.setReviewLikes(reviewLikeMap.get(r.getReviewId())));
    }

    private void insertReviewHospital(Page<ReviewSearchSelectQuery> result, List<Long> reviewIds) {
        List<ReviewSearchReviewHospitalDTO> reviewHospitalDto =
                queryFactory
                        .select(new QReviewSearchReviewHospitalDTO(review.id
                                , reviewHospital.content,
                                reviewHospital.disease,
                                reviewHospital.evCriteria.averageRate,
                                hospital.id,
                                hospital.hospitalName))
                        .from(reviewHospital)
                        .join(reviewHospital.review, review)
                        .join(reviewHospital.hospital, hospital)
                        .where(reviewHospital.review.id.in(reviewIds))
                        .fetch();

        Map<Long, List<ReviewSearchReviewHospitalDTO>> reviewHospitalMap =
                reviewHospitalDto
                .stream()
                .collect(Collectors.groupingBy(ReviewHospitalDTO -> ReviewHospitalDTO.getReviewId()));

        result.forEach(r -> r.setReviewHospitals(reviewHospitalMap.get(r.getReviewId())));
    }

    private boolean checkEmpty(Page<ReviewSearchSelectQuery> result) {
        return result != null && result.getContent().isEmpty();
    }
    

    private JPAQuery<Review> getTotalCount(String searchName) {
        JPAQuery<Review> countQuery = queryFactory
                .selectFrom(review)
                .join(review.member, member)
                .where(
                        reviewHospitalContent(searchName)
                                .or(reviewHospitalDisease(searchName))
                );
        return countQuery;
    }

    private BooleanExpression reviewHospitalContent(String name) {
        return isEmpty(name) ? null : review.reviewHospitals.any().content.contains(name);
    }

    private BooleanExpression reviewHospitalDisease(String name) {
        return isEmpty(name) ? null : review.reviewHospitals.any().disease.contains(name);
    }

}
