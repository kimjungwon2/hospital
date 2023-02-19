package site.hospital.hospital.user.repository;

import static org.springframework.util.ObjectUtils.isEmpty;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.hospital.user.domain.QHospitalThumbnail.hospitalThumbnail;
import static site.hospital.hospital.user.domain.QStaffHosInformation.staffHosInformation;
import static site.hospital.hospital.user.domain.detailedHosInformation.QDetailedHosInformation.detailedHosInformation;

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
