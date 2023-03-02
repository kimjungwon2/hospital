package site.hospital.hospital.user.repository;

import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.hospital.user.domain.QHospitalThumbnail.hospitalThumbnail;
import static site.hospital.hospital.user.domain.QStaffHosInformation.staffHosInformation;
import static site.hospital.hospital.user.domain.detailedinfo.QDetailedHosInformation.detailedHosInformation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import site.hospital.hospital.user.domain.Hospital;

public class HospitalRepositoryImpl implements HospitalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HospitalRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Hospital viewHospital(Long hospitalId) {
        return queryFactory
                .select(hospital)
                .from(hospital)
                .leftJoin(hospital.detailedHosInformation, detailedHosInformation).fetchJoin()
                .leftJoin(hospital.staffHosInformation, staffHosInformation).fetchJoin()
                .leftJoin(hospital.hospitalThumbnail, hospitalThumbnail).fetchJoin()
                .where(hospitalIdEq(hospitalId))
                .fetchOne();
    }

    public Hospital findHospitalByHosAdditionalInfoId(Long staffHosId) {
        return queryFactory
                .selectFrom(hospital)
                .where(hospitalAdditionalInfoIdEq(staffHosId))
                .fetchOne();
    }

    public Hospital findByDoctorId(Long doctorId) {
        return queryFactory
                .selectFrom(hospital)
                .where(doctorIdEq(doctorId))
                .fetchOne();
    }

    private BooleanExpression hospitalAdditionalInfoIdEq(Long id) {
        return id == null ? null : hospital.staffHosInformation.id.eq(id);
    }

    private BooleanExpression doctorIdEq(Long id) {
        return id == null ? null : hospital.staffHosInformation.doctors.any().id.eq(id);
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id != null ? hospital.id.eq(id) : null;
    }

}
