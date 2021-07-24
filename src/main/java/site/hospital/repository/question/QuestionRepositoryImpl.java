package site.hospital.repository.question;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.Question;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.QAnswer.answer;
import static site.hospital.domain.QQuestion.question;
import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.member.QMember.member;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Question> searchHospitalQuestion(Long hospitalId){
        List<Question> result = queryFactory
                .select(question)
                .from(question)
                .join(question.member, member).fetchJoin()
                .leftJoin(question.answer, answer).fetchJoin()
                .where(hospitalIdEq(hospitalId))
                .fetch();

        return result;
    }

    public Page<Question> searchQuestion(Long memberId, Long hospitalId, Pageable pageable){
        QueryResults<Question> result = queryFactory
                .select(question)
                .from(question)
                .join(question.member, member).fetchJoin()
                .join(question.hospital, hospital).fetchJoin()
                .where(memberIdEq(memberId), hospitalIdEq(hospitalId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Question> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    public Page<Question> adminSearchQuestion(Long memberId, Long hospitalId, Pageable pageable){
        QueryResults<Question> result = queryFactory
                .select(question)
                .from(question)
                .join(question.member, member).fetchJoin()
                .join(question.hospital, hospital).fetchJoin()
                .where(memberIdEq(memberId), hospitalIdEq(hospitalId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Question> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    private BooleanExpression memberIdEq(Long id){
        return id == null? null:question.member.id.eq(id);
    }
    private BooleanExpression hospitalIdEq(Long id){
        return id == null? null:question.hospital.id.eq(id);
    }

}
