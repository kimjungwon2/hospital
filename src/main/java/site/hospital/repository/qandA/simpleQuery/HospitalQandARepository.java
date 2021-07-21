package site.hospital.repository.qandA.simpleQuery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import site.hospital.repository.hospital.viewQuery.ViewHospitalDTO;

import javax.persistence.EntityManager;

import java.util.List;

import static site.hospital.domain.QAnswer.answer;
import static site.hospital.domain.QQandA.qandA;
import static site.hospital.domain.member.QMember.member;

@Repository
public class HospitalQandARepository {

    private final JPAQueryFactory queryFactory;

    public HospitalQandARepository(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    public List<SearchHospitalQandADTO> viewHospitalQandA(Long hospitalId) {

        List<SearchHospitalQandADTO> result = queryFactory
                .select(new QSearchHospitalQandADTO(qandA.id,member.nickName,
                        qandA.content, qandA.answer.id,qandA.answer.answerContent))
                .from(qandA)
                .join(qandA.member, member)
                .leftJoin(qandA.answer, answer)
                .where(hospitalIdEq(hospitalId))
                .fetch();
        return result;
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id == null? null:qandA.hospital.id.eq(id);
    }
}
