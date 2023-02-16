package site.hospital.estimation.admin.repository;

import static site.hospital.domain.estimation.QEstimation.estimation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import site.hospital.estimation.admin.domain.Estimation;
import site.hospital.estimation.admin.domain.EstimationList;
import site.hospital.hospital.user.domain.Hospital;

public class EstimationRepositoryImpl implements EstimationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EstimationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Estimation> searchEstimation(Hospital hospital, EstimationList estimationList) {
        List<Estimation> result = queryFactory
                .select(estimation)
                .from(estimation)
                .where(hospitalEq(hospital), cityNameEq(estimationList))
                .fetch();
        return result;
    }

    @Override
    public void adminDeleteEstimation(Hospital hospital) {
        queryFactory.delete(estimation)
                .where(hospitalEq(hospital))
                .execute();
    }

    private BooleanExpression hospitalEq(Hospital hospital) {
        return hospital == null ? null : estimation.hospital.eq(hospital);
    }

    private BooleanExpression cityNameEq(EstimationList estimationList) {
        return estimationList == null ? null : estimation.estimationList.eq(estimationList);
    }
}
