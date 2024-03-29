package site.hospital.question.user.repository.inquiry;

import static site.hospital.answer.user.domain.QAnswer.answer;
import static site.hospital.question.user.domain.QQuestion.question;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.member.user.domain.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;


@Repository
public class UserQuestionRepository {

    private final JPAQueryFactory queryFactory;

    public UserQuestionRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<UserQuestionSelectQuery> inquireQuestionsByUser(Long memberId) {
        return queryFactory
                .select(new QUserQuestionSelectQuery(
                        question.id,
                        hospital.id,
                        hospital.hospitalName,
                        member.nickName,
                        question.content,
                        question.answer.id,
                        answer.answerContent))
                .from(question)
                .join(question.member, member)
                .join(question.hospital, hospital)
                .leftJoin(question.answer, answer)
                .orderBy(answer.id.asc().nullsFirst())
                .where(hospitalIdEq(memberId))
                .fetch();
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id == null ? null : question.member.id.eq(id);
    }
}
