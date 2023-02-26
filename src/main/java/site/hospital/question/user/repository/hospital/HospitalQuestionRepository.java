package site.hospital.question.user.repository.hospital;

import static site.hospital.answer.manager.domain.QAnswer.answer;
import static site.hospital.question.user.domain.QQuestion.question;
import static site.hospital.member.user.domain.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public HospitalQuestionRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<HospitalQuestionSelectQuery> inquireHospitalQuestions(Long hospitalId) {

        List<HospitalQuestionSelectQuery> result = queryFactory
                .select(new QHospitalQuestionSelectQuery(
                        question.id,
                        member.nickName,
                        question.content,
                        question.answer.id,
                        answer.answerContent))
                .from(question)
                .join(question.member, member)
                .leftJoin(question.answer, answer)
                .where(hospitalIdEq(hospitalId))
                .orderBy(answer.id.asc().nullsLast())
                .fetch();
        return result;
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id == null ? null : question.hospital.id.eq(id);
    }
}
