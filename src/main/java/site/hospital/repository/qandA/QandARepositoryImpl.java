package site.hospital.repository.qandA;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.QandA;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.QAnswer.answer;
import static site.hospital.domain.QQandA.qandA;
import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.member.QMember.member;

public class QandARepositoryImpl implements QandARepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public QandARepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<QandA> searchHospitalQandA(Long hospitalId){
        List<QandA> result = queryFactory
                .select(qandA)
                .from(qandA)
                .join(qandA.member, member).fetchJoin()
                .leftJoin(qandA.answer, answer).fetchJoin()
                .where(hospitalIdEq(hospitalId))
                .fetch();

        return result;
    }

    public List<QandA> searchQandA(Long memberId, Long hospitalId){
        List<QandA> result = queryFactory
                .select(qandA)
                .from(qandA)
                .join(qandA.member, member).fetchJoin()
                .join(qandA.hospital, hospital).fetchJoin()
                .where(memberIdEq(memberId), hospitalIdEq(hospitalId))
                .fetch();

        return result;
    }

    private BooleanExpression memberIdEq(Long id){
        return id == null? null:qandA.member.id.eq(id);
    }
    private BooleanExpression hospitalIdEq(Long id){
        return id == null? null:qandA.hospital.id.eq(id);
    }

}
