package site.hospital.question.repository.adminSearchQuery;

import static site.hospital.domain.QAnswer.answer;
import static site.hospital.domain.QQuestion.question;
import static site.hospital.domain.hospital.QHospital.hospital;
import static site.hospital.domain.member.QMember.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.hospital.question.repository.dto.AdminQuestionSearchCondition;
import site.hospital.repository.question.adminSearchQuery.QAdminSearchQuestionDto;


@Repository
public class AdminQuestionSearchRepository {

    private final JPAQueryFactory queryFactory;

    public AdminQuestionSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Page<AdminSearchQuestionDto> adminSearchQuestions(AdminQuestionSearchCondition condition,
            Pageable pageable) {
        QueryResults<AdminSearchQuestionDto> result = queryFactory
                .select(new QAdminSearchQuestionDto(question.id, member.memberIdName,
                        member.nickName, hospital.hospitalName, question.content, answer.id,
                        answer.answerContent))
                .from(question)
                .join(question.member, member)
                .leftJoin(question.answer, answer)
                .join(question.hospital, hospital)
                .where(memberIdNameLike(condition.getMemberIdName()),
                        nickNameEq(condition.getNickName()),
                        hospitalNameLike(condition.getHospitalName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminSearchQuestionDto> content = result.getResults();
        Long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression memberIdNameLike(String memberIdName) {
        return memberIdName == null ? null : question.member.memberIdName.contains(memberIdName);
    }

    private BooleanExpression nickNameEq(String nickName) {
        return nickName == null ? null : question.member.nickName.eq(nickName);
    }

    private BooleanExpression hospitalNameLike(String hospitalName) {
        return hospitalName == null ? null : question.hospital.hospitalName.contains(hospitalName);
    }
}
