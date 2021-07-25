package site.hospital.repository.hospital;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.Hospital;
import site.hospital.repository.hospital.adminSearchQuery.QAdminSearchHospitalDto;

import javax.persistence.EntityManager;

import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.QStaffHosInformation.staffHosInformation;
import static site.hospital.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;

public class HospitalRepositoryImpl implements HospitalRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public HospitalRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Hospital adminViewHospital(Long hospitalId){
        Hospital result = queryFactory
                .select(hospital)
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation)
                .leftJoin(hospital.staffHosInformation, staffHosInformation)
                .where(hospitalIdEq(hospitalId))
                .fetchOne();
        return result;
    }

    public Hospital findByStaffHosId(Long staffHosId){
        Hospital result = queryFactory
                .selectFrom(hospital)
                .where(hospital.staffHosInformation.id.eq(staffHosId))
                .fetchOne();
        return result;
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id != null? hospital.id.eq(id): null;
    }

}
