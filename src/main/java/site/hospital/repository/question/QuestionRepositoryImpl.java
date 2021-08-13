package site.hospital.repository.question;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.Question;
import site.hospital.dto.StaffQuestionSearchCondition;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.QAnswer.answer;
import static site.hospital.domain.QQuestion.question;
import static site.hospital.domain.hospital.QHospital.hospital;
import static site.hospital.domain.member.QMember.member;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Question> staffSearchHospitalQuestion(Long hospitalId, StaffQuestionSearchCondition condition, Pageable pageable){
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

        List<Question> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    //답변이 없는 경우 질문의 수
    @Override
    public Long staffQuestionNoAnswer(Long hospitalId){

        return queryFactory.select(question)
                .from(question)
                .where(hospitalIdEq(hospitalId), question.answer.isNull())
                .fetchCount();
    }

    @Override
    public Page<Question> staffSearchNoQuestion(Long hospitalId, StaffQuestionSearchCondition condition, Pageable pageable){
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

        List<Question> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<Question> searchQuestion(Long memberId, Pageable pageable){
        QueryResults<Question> result = queryFactory
                .select(question)
                .from(question)
                .join(question.member, member).fetchJoin()
                .join(question.hospital, hospital).fetchJoin()
                .leftJoin(question.answer, answer).fetchJoin()
                .where(memberIdEq(memberId))
                .orderBy(answer.id.asc().nullsFirst())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Question> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public void adminDeleteQuestion(Hospital hospital){
        queryFactory.delete(question)
                .where(hospitalEq(hospital))
                .execute();
    }

    private BooleanExpression memberIdNameLike(String memberIdName){
        return memberIdName==null?  null: member.memberIdName.contains(memberIdName);
    }

    private BooleanExpression hospitalEq(Hospital hospital){
        return hospital==null?  null: question.hospital.eq(hospital);
    }

    private BooleanExpression nickNameEq(String nickName){
        return nickName==null?  null: member.nickName.eq(nickName);
    }


    private BooleanExpression memberIdEq(Long id){
        return id == null? null:question.member.id.eq(id);
    }
    private BooleanExpression hospitalIdEq(Long id){
        return id == null? null:question.hospital.id.eq(id);
    }
}
