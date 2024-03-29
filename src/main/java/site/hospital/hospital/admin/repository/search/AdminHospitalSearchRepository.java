package site.hospital.hospital.admin.repository.search;

import static site.hospital.hospital.user.domain.QHospitalThumbnail.hospitalThumbnail;
import static site.hospital.hospital.user.domain.QStaffHosInformation.staffHosInformation;
import static site.hospital.hospital.user.domain.detailedinfo.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.hospital.user.domain.QHospital.hospital;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.hospital.hospital.user.domain.BusinessCondition;
import site.hospital.hospital.admin.repository.dto.AdminHospitalSearchCondition;

@Repository
public class AdminHospitalSearchRepository {

    private final JPAQueryFactory queryFactory;

    public AdminHospitalSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<AdminHospitalSearchSelectQuery> adminSearchHospitals(
            AdminHospitalSearchCondition condition,
            Pageable pageable
    ) {
        QueryResults<AdminHospitalSearchSelectQuery> result = queryFactory
                .select(new QAdminHospitalSearchSelectQuery(
                        hospital.id,
                        detailedHosInformation.id,
                        staffHosInformation.id,
                        hospitalThumbnail.id,
                        hospital.hospitalName,
                        hospital.businessCondition,
                        hospital.cityName,
                        hospital.phoneNumber))
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation)
                .leftJoin(hospital.staffHosInformation, staffHosInformation)
                .leftJoin(hospital.hospitalThumbnail, hospitalThumbnail)
                .where(hospitalIdEq(condition.getHospitalId()),
                        hospitalNameLike(condition.getHospitalName()),
                        businessConditionEq(condition.getBusinessCondition()),
                        cityNameEq(condition.getCityName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminHospitalSearchSelectQuery> content = result.getResults();
        Long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression hospitalIdEq(Long hospitalId) {
        return hospitalId == null ? null : hospital.id.eq(hospitalId);
    }

    private BooleanExpression hospitalNameLike(String hospitalName) {
        return hospitalName == null ? null : hospital.hospitalName.contains(hospitalName);
    }

    private BooleanExpression businessConditionEq(BusinessCondition businessCondition) {
        return businessCondition == null ? null : hospital.businessCondition.eq(businessCondition);
    }

    private BooleanExpression cityNameEq(String cityName) {
        return cityName == null ? null : hospital.cityName.eq(cityName);
    }
}
