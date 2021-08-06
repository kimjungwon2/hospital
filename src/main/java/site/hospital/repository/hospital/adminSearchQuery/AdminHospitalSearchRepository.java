package site.hospital.repository.hospital.adminSearchQuery;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.hospital.dto.AdminHospitalSearchCondition;

import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;
import static site.hospital.domain.QStaffHosInformation.staffHosInformation;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class AdminHospitalSearchRepository {
    private final JPAQueryFactory queryFactory;

    public AdminHospitalSearchRepository(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}


    public Page<AdminSearchHospitalDto> adminSearchHospitals(AdminHospitalSearchCondition condition, Pageable pageable){
        QueryResults<AdminSearchHospitalDto> result = queryFactory
                .select(new QAdminSearchHospitalDto(
                        hospital.id, detailedHosInformation.id,
                        staffHosInformation.id, hospital.hospitalName,
                        hospital.businessCondition,
                        hospital.cityName,
                        hospital.phoneNumber))
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation)
                .leftJoin(hospital.staffHosInformation, staffHosInformation)
                .where(hospitalIdEq(condition.getHospitalId()),
                        hospitalNameLike(condition.getHospitalName()),
                        businessConditionEq(condition.getBusinessCondition()),
                        cityNameEq(condition.getCityName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminSearchHospitalDto> content = result.getResults();
        Long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    private BooleanExpression hospitalIdEq(Long hospitalId){
        return hospitalId==null?  null: hospital.id.eq(hospitalId);
    }

    private BooleanExpression hospitalNameLike(String hospitalName){
        return hospitalName==null?  null: hospital.hospitalName.contains(hospitalName);
    }
    private BooleanExpression businessConditionEq(String businessCondition){
        return businessCondition==null?  null: hospital.businessCondition.eq(businessCondition);
    }
    private BooleanExpression cityNameEq(String cityName){
        return cityName==null?  null: hospital.cityName.eq(cityName);
    }
}
