package site.hospital.hospital.user.repository;

import static org.springframework.util.ObjectUtils.isEmpty;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.hospital.user.domain.QHospitalThumbnail.hospitalThumbnail;
import static site.hospital.hospital.user.domain.QStaffHosInformation.staffHosInformation;
import static site.hospital.hospital.user.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.review.user.domain.reviewHospital.QReviewHospital.reviewHospital;
import static site.hospital.tag.manager.domain.QPostTag.postTag;
import static site.hospital.tag.manager.domain.QTag.tag;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.repository.searchQuery.HospitalSearchDto;
import site.hospital.hospital.user.repository.searchQuery.PostTagDto;
import site.hospital.hospital.user.repository.searchQuery.QHospitalSearchDto;
import site.hospital.hospital.user.repository.searchQuery.QPostTagDto;
import site.hospital.hospital.user.repository.searchQuery.QReviewHospitalDto;
import site.hospital.hospital.user.repository.searchQuery.ReviewHospitalDto;

public class HospitalRepositoryImpl implements HospitalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HospitalRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Hospital viewHospital(Long hospitalId) {
        Hospital result = queryFactory
                .select(hospital)
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation).fetchJoin()
                .leftJoin(hospital.staffHosInformation, staffHosInformation).fetchJoin()
                .leftJoin(hospital.hospitalThumbnail, hospitalThumbnail).fetchJoin()
                .where(hospitalIdEq(hospitalId))
                .fetchOne();
        return result;
    }

    public Hospital findByStaffHosId(Long staffHosId) {
        Hospital result = queryFactory
                .selectFrom(hospital)
                .where(staffHosInEq(staffHosId))
                .fetchOne();
        return result;
    }

    public Hospital findByDoctorId(Long doctorId) {
        Hospital result = queryFactory
                .selectFrom(hospital)
                .where(doctorIdEq(doctorId))
                .fetchOne();

        return result;
    }

    public Page<Hospital> searchHospitalVer2(String searchName, Pageable pageable){
        QueryResults<Hospital> result = queryFactory
                .selectFrom(hospital)
                .leftJoin(hospital.detailedHosInformation,detailedHosInformation).fetchJoin()
                .leftJoin(hospital.hospitalThumbnail,hospitalThumbnail).fetchJoin()
                .where((hospitalNameLike(searchName)
                        .or(hospitalSubjectLike(searchName)
                        .or(tagNameLike(searchName)
                        ))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Hospital> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<HospitalSearchDto> searchHospitalVer1(String searchName, Pageable pageable) {
        Page<HospitalSearchDto> result =  findHospital(searchName, pageable);

        result.forEach(hospital -> {
            List<ReviewHospitalDto> reviewHospitals =
                    findReviewHospitals(hospital.getHospitalId());
            hospital.setReviewHospitals(reviewHospitals);
        });

        result.forEach(hospital -> {
            List<PostTagDto> postTags =
                    findPostTags(hospital.getHospitalId());
            hospital.setPostTagDtos(postTags);
        });

        return result;
    }

    private Page<HospitalSearchDto> findHospital(String searchName, Pageable pageable) {
        List<HospitalSearchDto> content = queryFactory
                .select(new QHospitalSearchDto(hospital.id,
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

        //쿼리 수 세기
        JPAQuery<Hospital> countQuery = queryFactory
                .select(hospital)
                .from(hospital)
                .join(hospital.detailedHosInformation, detailedHosInformation)
                .where((hospitalNameLike(searchName)
                        .or(hospitalSubjectLike(searchName)
                                .or(tagNameLike(searchName)
                                ))));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    private List<ReviewHospitalDto> findReviewHospitals(Long hospitalId){
        return queryFactory
                .select(new QReviewHospitalDto(reviewHospital.hospital.id,
                        reviewHospital.evCriteria.averageRate.avg(),
                        reviewHospital.count()))
                .from(reviewHospital)
                .join(reviewHospital.hospital, hospital)
                .groupBy(reviewHospital.hospital.id)
                .where(reviewHospital.hospital.id.eq(hospitalId))
                .fetch();
    }

    private List<PostTagDto> findPostTags(Long hospitalId){
        return queryFactory
                .select(new QPostTagDto(postTag.hospital.id, tag.id, tag.name))
                .from(postTag)
                .join(postTag.tag, tag)
                .where(postTag.hospital.id.eq(hospitalId))
                .fetch();
    }


    private BooleanExpression staffHosInEq(Long id) {
        return id == null ? null : hospital.staffHosInformation.id.eq(id);
    }

    private BooleanExpression doctorIdEq(Long id) {
        return id == null ? null : hospital.staffHosInformation.doctors.any().id.eq(id);
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id != null ? hospital.id.eq(id) : null;
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
