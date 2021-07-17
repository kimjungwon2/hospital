package site.hospital.repository.hospital.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import site.hospital.domain.Hospital;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;
import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.QPostTag.postTag;
import static site.hospital.domain.QTag.tag;
import static site.hospital.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.domain.reviewHospital.QReviewHospital.reviewHospital;


@Repository
public class HospitalSearchRepository {
    private final JPAQueryFactory queryFactory;

    public HospitalSearchRepository(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}


    public Page<HospitalSearchDto> searchHospital(HospitalSearchCondition condition,Pageable pageable){
        Page<HospitalSearchDto> result = findHospitals(condition, pageable);

        List<Long> hospitalIds = result.stream()
                .map(h-> h.getHospitalId())
                .collect(Collectors.toList());

        List<PostTagDto> postTagDtos =
                queryFactory
                .select(new QPostTagDto(postTag.hospital.id, postTag.tag.name))
                .from(postTag)
                .join(postTag.tag, tag)
                .where(postTag.hospital.id.in(hospitalIds))
                .fetch();

        Map<Long, List<PostTagDto>> tagHospitalMap = postTagDtos.stream()
                .collect(Collectors.groupingBy(PostTagDto -> PostTagDto.getHospitalId()));

        result.forEach(h->h.setPostTagDtos(tagHospitalMap.get(h.getHospitalId())));


        List<ReviewHospitalDto> reviewHospitalDtos =
                queryFactory
                .select(new QReviewHospitalDto(reviewHospital.hospital.id,reviewHospital.evCriteria.sumPrice))
                .from(reviewHospital)
                .join(reviewHospital.hospital, hospital)
                .where(reviewHospital.hospital.id.in(hospitalIds))
                .fetch();

        Map<Long, List<ReviewHospitalDto>> reviewHospitalMap = reviewHospitalDtos.stream()
                .collect(Collectors.groupingBy(reviewHospitalDto -> reviewHospitalDto.getHospitalId()));

        result.forEach(h->h.setReviewHospitals(reviewHospitalMap.get(h.getHospitalId())));

        return result;

    }


    private Page<HospitalSearchDto> findHospitals(HospitalSearchCondition condition, Pageable pageable){
        List<HospitalSearchDto> content =  queryFactory
                .select(new QHospitalSearchDto(hospital.id,
                        hospital.hospitalName,
                        hospital.businessCondition,
                        hospital.medicalSubjectInformation,
                        detailedHosInformation.hospitalAddress.roadBaseAddress
                        )
                )
                .from(hospital)
                .join(hospital.detailedHosInformation, detailedHosInformation)
                .where( (hospitalNameLike(condition.getSearchName())
                        .or(hospitalSubjectLike(condition.getSearchName())
                        .or(tagNameLike(condition.getSearchName()))))
                      )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Hospital> countQuery= queryFactory
                .select(hospital)
                .from(hospital)
                .join(hospital.detailedHosInformation, detailedHosInformation)
                .where( (hospitalNameLike(condition.getSearchName())
                        .or(hospitalSubjectLike(condition.getSearchName())
                                .or(tagNameLike(condition.getSearchName())))));


        return PageableExecutionUtils.getPage(content, pageable, ()-> countQuery.fetchCount());
    }

    private BooleanExpression hospitalNameLike(String name){
        return isEmpty(name)?  null: hospital.hospitalName.contains(name);
    }
    private BooleanExpression hospitalSubjectLike(String name){
        return isEmpty(name)?  null: hospital.medicalSubjectInformation.contains(name);
    }
    private BooleanExpression tagNameLike(String name){
        return isEmpty(name)?  null: hospital.postTags.any().tag.name.contains(name);
    }

}
