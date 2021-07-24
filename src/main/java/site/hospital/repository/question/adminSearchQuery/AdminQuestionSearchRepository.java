package site.hospital.repository.question.adminSearchQuery;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.hospital.dto.AdminQuestionSearchCondition;



import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.QHospital.hospital;
import static site.hospital.domain.QQuestion.question;
import static site.hospital.domain.member.QMember.member;
import static site.hospital.domain.QAnswer.answer;


@Repository
public class AdminQuestionSearchRepository {
    private final JPAQueryFactory queryFactory;

    public AdminQuestionSearchRepository(EntityManager em){ this.queryFactory = new JPAQueryFactory(em);}

    public Page<AdminSearchQuestionDto> adminQuestions(Pageable pageable){
        QueryResults<AdminSearchQuestionDto> result = queryFactory
                .select(new QAdminSearchQuestionDto(question.id, member.memberIdName,
                        member.nickName, hospital.hospitalName, question.content, answer.answerContent))
                .from(question)
                .join(question.member, member)
                .leftJoin(question.answer, answer)
                .join(question.hospital, hospital)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminSearchQuestionDto> content = result.getResults();
        Long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    public Page<AdminSearchQuestionDto> adminSearchQuestions(AdminQuestionSearchCondition condition, Pageable pageable){
        QueryResults<AdminSearchQuestionDto> result = queryFactory
                .select(new QAdminSearchQuestionDto(question.id, member.memberIdName,
                        member.nickName, hospital.hospitalName, question.content, answer.answerContent))
                .from(question)
                .join(question.member, member)
                .leftJoin(question.answer, answer)
                .join(question.hospital, hospital)
                .where(memberIdNameLike(condition.getMemberIdName()),
                        nickNameEq(condition.getNickName()),hospitalNameLike(condition.getHospitalName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminSearchQuestionDto> content = result.getResults();
        Long total = result.getTotal();

        return new PageImpl<>(content,pageable,total);
    }


    private BooleanExpression memberIdNameLike(String memberIdName){
        return memberIdName==null?  null: question.member.memberIdName.contains(memberIdName);
    }
    private BooleanExpression nickNameEq(String nickName){
        return nickName==null?  null: question.member.nickName.eq(nickName);
    }
    private BooleanExpression hospitalNameLike(String hospitalName){
        return hospitalName==null?  null: question.hospital.hospitalName.contains(hospitalName);
    }
}
