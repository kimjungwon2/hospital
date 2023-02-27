package site.hospital.hospital.user.repository.search;

import static org.springframework.util.ObjectUtils.isEmpty;
import static site.hospital.hospital.user.domain.QHospitalThumbnail.hospitalThumbnail;
import static site.hospital.tag.manager.domain.QPostTag.postTag;
import static site.hospital.tag.manager.domain.QTag.tag;
import static site.hospital.hospital.user.domain.detailedinfo.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.review.user.domain.reviewHospital.QReviewHospital.reviewHospital;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.MathExpressions;
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
import site.hospital.hospital.user.domain.Hospital;

@Repository
public class HospitalSearchRepository {

    private final JPAQueryFactory queryFactory;

    public HospitalSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Page<HospitalSearchSelectQuery> searchHospitals(String searchName, Pageable pageable) {
        Page<HospitalSearchSelectQuery> searchResults = findHospitals(searchName, pageable);

        if (checkEmpty(searchResults)) {
            return searchResults;
        }

        List<Long> hospitalIds = searchResults
                .stream()
                .map(h -> h.getHospitalId())
                .collect(Collectors.toList());

        insertReviewHospitals(searchResults, hospitalIds);
        insertPostTags(searchResults, hospitalIds);

        return searchResults;
    }

    private void insertPostTags(Page<HospitalSearchSelectQuery> searchResults, List<Long> hospitalIds) {
        List<HospitalSearchPostTagDTO> hospitalSearchPostTagDTOS =
                queryFactory
                        .select(new QHospitalSearchPostTagDTO(postTag.hospital.id, tag.id, tag.name))
                        .from(postTag)
                        .join(postTag.tag, tag)
                        .where(postTag.hospital.id.in(hospitalIds))
                        .fetch();

        Map<Long, List<HospitalSearchPostTagDTO>> tagHospitalMap = hospitalSearchPostTagDTOS.stream()
                .collect(Collectors.groupingBy(HospitalSearchPostTagDTO -> HospitalSearchPostTagDTO.getHospitalId()));

        searchResults.forEach(h -> h.setHospitalSearchPostTagDTOS(tagHospitalMap.get(h.getHospitalId())));
    }

    private void insertReviewHospitals(Page<HospitalSearchSelectQuery> searchResults, List<Long> hospitalIds) {
        List<HospitalSearchReviewHospitalDTO> hospitalSearchReviewHospitalDTOS =
                queryFactory
                        .select(new QHospitalSearchReviewHospitalDTO(
                                reviewHospital.hospital.id,
                                MathExpressions.round(reviewHospital.evCriteria.averageRate.avg(),2),
                                reviewHospital.count()))
                        .from(reviewHospital)
                        .join(reviewHospital.hospital, hospital)
                        .groupBy(reviewHospital.hospital.id)
                        .where(reviewHospital.hospital.id.in(hospitalIds))
                        .fetch();

        Map<Long, List<HospitalSearchReviewHospitalDTO>> reviewHospitalMap = hospitalSearchReviewHospitalDTOS.stream()
                .collect(Collectors
                        .groupingBy(
                                hospitalSearchReviewHospitalDTO -> hospitalSearchReviewHospitalDTO.getHospitalId()));

        searchResults.forEach(h -> h.setReviewHospitals(reviewHospitalMap.get(h.getHospitalId())));
    }

    private Page<HospitalSearchSelectQuery> findHospitals(String searchName, Pageable pageable) {
        List<HospitalSearchSelectQuery> content = queryFactory
                .select(new QHospitalSearchSelectQuery(hospital.id,
                                hospital.hospitalName,
                                hospitalThumbnail.imageKey,
                                hospital.businessCondition,
                                hospital.medicalSubjectInformation,
                                detailedHosInformation.hospitalAddress.roadBaseAddress
                        )
                )
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation)
                .leftJoin(hospital.hospitalThumbnail, hospitalThumbnail)
                .where((hospitalNameLike(searchName)
                        .or(hospitalSubjectLike(searchName)
                                .or(tagNameLike(searchName))))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Hospital> countQuery = getCountQuery(searchName);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    private JPAQuery<Hospital> getCountQuery(String searchName) {
        JPAQuery<Hospital> countQuery = queryFactory
                .select(hospital)
                .from(hospital)
                .join(hospital.detailedHosInformation, detailedHosInformation)
                .where((hospitalNameLike(searchName)
                        .or(hospitalSubjectLike(searchName)
                                .or(tagNameLike(searchName)
                                ))));
        return countQuery;
    }

    private boolean checkEmpty(Page<HospitalSearchSelectQuery> result) {
        return result.getContent() != null && result.getContent().isEmpty();
    }


    private BooleanExpression hospitalNameLike(String name) {
        return isEmpty(name) ? null : hospital.hospitalName.contains(name);
    }

    private BooleanExpression hospitalSubjectLike(String name) {
        return isEmpty(name) ? null : hospital.medicalSubjectInformation.contains(name);
    }

    private BooleanExpression tagNameLike(String name) {
        return isEmpty(name) ? null : hospital.postTags.any().tag.name.eq(name);
    }

}
