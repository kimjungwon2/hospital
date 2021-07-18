package site.hospital.repository.hospital;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.Hospital;

import javax.persistence.EntityManager;
import java.util.List;


import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;

public class HospitalRepositoryImpl implements HospitalRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public HospitalRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Hospital hospitalInformation(Long hospitalId){
        Hospital result = queryFactory
                .selectFrom(hospital)
                .join(hospital.detailedHosInformation, detailedHosInformation).fetchJoin()
                .where(hospitalIdEq(hospitalId))
                .fetchOne();

        return result;
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id != null? hospital.id.eq(id): null;
    }
}
