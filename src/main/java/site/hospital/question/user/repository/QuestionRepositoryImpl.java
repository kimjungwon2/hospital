package site.hospital.question.user.repository;

import static site.hospital.answer.user.domain.QAnswer.answer;
import static site.hospital.member.user.domain.QMember.member;
import static site.hospital.question.user.domain.QQuestion.question;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.question.manager.repository.dto.ManagerQuestionSearchCondition;
import site.hospital.question.user.domain.Question;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Question> managerSearchHospitalQuestion(
            Long hospitalId,
            ManagerQuestionSearchCondition condition,
            Pageable pageable
    ) {
        QueryResults<Question> result = queryFactory
                .select(question)
                .from(question)
                .join(question.member, member).fetchJoin()
                .leftJoin(question.answer, answer).fetchJoin()
                .where(hospitalIdEq(hospitalId),
                        memberIdNameLike(condition.getMemberIdName()),
                        nickNameEq(condition.getNickName()))
                .orderBy(answer.id.asc().nullsFirst())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return getQuestionsPage(pageable, result);
    }

    @Override
    public Long managerCountQuestionsWithNoAnswer(Long hospitalId) {

        return queryFactory
                .select(question)
                .from(question)
                .where(hospitalIdEq(hospitalId), question.answer.isNull())
                .fetchCount();
    }

    @Override
    public Page<Question> managerSearchNoQuestions(
            Long hospitalId,
            ManagerQuestionSearchCondition condition,
            Pageable pageable
    ) {
        QueryResults<Question> result = queryFactory
                .select(question)
                .from(question)
                .join(question.member, member).fetchJoin()
                .leftJoin(question.answer, answer).fetchJoin()
                .where(hospitalIdEq(hospitalId), question.answer.isNull(),
                        memberIdNameLike(condition.getMemberIdName()),
                        nickNameEq(condition.getNickName()))
                .orderBy(answer.id.asc().nullsFirst())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return getQuestionsPage(pageable, result);
    }

    @Override
    public void adminDeleteQuestion(Hospital hospital) {
        queryFactory
                .delete(question)
                .where(hospitalEq(hospital))
                .execute();
    }

    private PageImpl<Question> getQuestionsPage(Pageable pageable, QueryResults<Question> result) {
        List<Question> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression memberIdNameLike(String memberIdName) {
        return memberIdName == null ? null : member.memberIdName.contains(memberIdName);
    }

    private BooleanExpression hospitalEq(Hospital hospital) {
        return hospital == null ? null : question.hospital.eq(hospital);
    }

    private BooleanExpression nickNameEq(String nickName) {
        return nickName == null ? null : member.nickName.eq(nickName);
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id == null ? null : question.hospital.id.eq(id);
    }
}
