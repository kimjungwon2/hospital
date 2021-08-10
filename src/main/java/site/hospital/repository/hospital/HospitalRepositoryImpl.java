package site.hospital.repository.hospital;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.hospital.Hospital;

import javax.persistence.EntityManager;

import static site.hospital.domain.hospital.QHospital.hospital;
import static site.hospital.domain.QStaffHosInformation.staffHosInformation;
import static site.hospital.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;

public class HospitalRepositoryImpl implements HospitalRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public HospitalRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Hospital viewHospital(Long hospitalId){
        Hospital result = queryFactory
                .select(hospital)
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation).fetchJoin()
                .leftJoin(hospital.staffHosInformation, staffHosInformation).fetchJoin()
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

    public Hospital findByDoctorId(Long doctorId){
        Hospital result = queryFactory
                .selectFrom(hospital)
                .where(hospital.staffHosInformation.doctors.any().id.eq(doctorId))
                .fetchOne();
        return result;
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id != null? hospital.id.eq(id): null;
    }

}
