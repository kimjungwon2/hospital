package site.hospital.repository.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.member.Authorization;
import site.hospital.domain.member.Member;
import site.hospital.dto.AdminMemberSearchCondition;

import static site.hospital.domain.member.QMember.member;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Member> adminMembers(Pageable pageable) {
        QueryResults<Member> result = queryFactory
                .select(member)
                .from(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Member> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);

    }


    @Override
    public Page<Member> adminSearchMembers(AdminMemberSearchCondition condition, Pageable pageable){

        //모두 검색(아이디+닉네임+유저이름+폰번호)
        if(condition.getAllSearch() != null){
            QueryResults<Member> result = queryFactory
                    .select(member)
                    .from(member)
                    .where(
                            memberNickNameEq(condition.getAllSearch())
                           .or(memberIdNameLike(condition.getAllSearch()))
                           .or(memberUserNameEq(condition.getAllSearch()))
                           .or(memberPhoneNumberEq(condition.getAllSearch()))
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            List<Member> content = result.getResults();
            long total = result.getTotal();

            return new PageImpl<>(content, pageable, total);
        }
        //각자 검색
        else{
            QueryResults<Member> result = queryFactory
                    .select(member)
                    .from(member)
                    .where(
                            memberIdEq(condition.getMemberId())
                            ,(memberIdNameLike(condition.getMemberIdName()))
                            ,(memberNickNameEq(condition.getNickName()))
                            ,(memberUserNameEq(condition.getUserName()))
                            ,(memberPhoneNumberEq(condition.getPhoneNumber()))
                            ,(memberAuthorizationStatusEq(condition.getAuthorizationStatus()))
                            ,(memberHospitalNumberEq(condition.getHospitalNumber()))
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            List<Member> content = result.getResults();
            long total = result.getTotal();

            return new PageImpl<>(content, pageable, total);
        }

    }

    private BooleanExpression memberIdEq(Long id){
        return id == null? null: member.id.eq(id);
    }
    private BooleanExpression memberIdNameLike(String memberIdName){
        return memberIdName ==null ? null: member.memberIdName.contains(memberIdName) ;
    }
    private BooleanExpression memberNickNameEq(String nickName){
        return nickName == null ? null: member.nickName.eq(nickName);
    }
    private BooleanExpression memberUserNameEq(String userName){
        return userName == null ? null: member.userName.eq(userName);
    }
    private BooleanExpression memberPhoneNumberEq(String phoneNumber){
        return phoneNumber == null? null: member.phoneNumber.eq(phoneNumber) ;
    }
    private BooleanExpression memberAuthorizationStatusEq(Authorization authorizationStatus){
        return authorizationStatus == null? null: member.authorizationStatus.eq(authorizationStatus);
    }
    private BooleanExpression memberHospitalNumberEq(Long hospitalNumber){
        return hospitalNumber == null? null: member.hospitalNumber.eq(hospitalNumber);
    }

}
