package site.hospital.repository.estimation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.Estimation;
import site.hospital.domain.Hospital;
import site.hospital.domain.member.Member;

import static org.springframework.util.StringUtils.hasText;
import static site.hospital.domain.QEstimation.estimation;
import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.QQuestion.question;
import static site.hospital.domain.member.QMemberAuthority.memberAuthority;

import javax.persistence.EntityManager;
import java.util.List;

public class EstimationRepositoryImpl implements EstimationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public EstimationRepositoryImpl(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    public List<Estimation> searchEstimation(String hospitalName, String cityName){
        List<Estimation> result =queryFactory
                .select(estimation)
                .from(estimation)
                .join(estimation.hospital, hospital).fetchJoin()
                .where(hospitalNameEq(hospitalName),cityNameEq(cityName))
                .fetch();

        return result;
    }

    @Override
    public void adminDeleteEstimation(Hospital hospital){
        queryFactory.delete(estimation)
                .where(estimation.hospital.eq(hospital))
                .execute();
    }


    private BooleanExpression hospitalNameEq(String hospitalName){
        return hasText(hospitalName) ? estimation.hospital.hospitalName.eq(hospitalName): null;
    }
    private BooleanExpression cityNameEq(String cityName){
        return hasText(cityName)? estimation.hospital.cityName.eq(cityName): null;
    }
}
