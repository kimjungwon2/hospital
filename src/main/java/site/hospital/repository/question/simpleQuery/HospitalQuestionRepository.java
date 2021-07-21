package site.hospital.repository.question.simpleQuery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import site.hospital.repository.question.simpleQuery.QSearchHospitalQuestionDTO;

import javax.persistence.EntityManager;

import java.util.List;

import static site.hospital.domain.QAnswer.answer;
import static site.hospital.domain.QQuestion.question;
import static site.hospital.domain.member.QMember.member;

@Repository
public class HospitalQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public HospitalQuestionRepository(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    public List<SearchHospitalQuestionDTO> viewHospitalQuestion(Long hospitalId) {

        List<SearchHospitalQuestionDTO> result = queryFactory
                .select(new QSearchHospitalQuestionDTO(question.id,member.nickName,
                        question.content, question.answer.id,question.answer.answerContent))
                .from(question)
                .join(question.member, member)
                .leftJoin(question.answer, answer)
                .where(hospitalIdEq(hospitalId))
                .fetch();
        return result;
    }

    private BooleanExpression hospitalIdEq(Long id){
        return id == null? null:question.hospital.id.eq(id);
    }
}
