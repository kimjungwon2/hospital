package site.hospital.repository.estimation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.estimation.Estimation;
import site.hospital.domain.estimation.EstimationList;
import site.hospital.domain.hospital.Hospital;

import static org.springframework.util.StringUtils.hasText;
import static site.hospital.domain.estimation.QEstimation.estimation;

import javax.persistence.EntityManager;
import java.util.List;

public class EstimationRepositoryImpl implements EstimationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public EstimationRepositoryImpl(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    public List<Estimation> searchEstimation(Hospital hospital, EstimationList estimationList){
        List<Estimation> result =queryFactory
                .select(estimation)
                .from(estimation)
                .where(hospitalEq(hospital),cityNameEq(estimationList))
                .fetch();
        return result;
    }

    @Override
    public void adminDeleteEstimation(Hospital hospital){
        queryFactory.delete(estimation)
                .where(estimation.hospital.eq(hospital))
                .execute();
    }


    private BooleanExpression hospitalEq(Hospital hospital){
        return hospital == null ? null: estimation.hospital.eq(hospital);
    }
    private BooleanExpression cityNameEq(EstimationList estimationList){
        return estimationList == null? null: estimation.estimationList.eq(estimationList);
    }
}
