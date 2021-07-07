package site.hospital.repository.member.simplequery;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.member.Authorization;
import site.hospital.repository.member.MemberRepositoryCustom;

import static site.hospital.domain.QMember.member;
import static org.springframework.util.StringUtils.hasText;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberSearchResult> search(MemberSearchCondition condition, Pageable pageable){

        //모두 검색
        if(condition.getAllSearch() != null){
            QueryResults<MemberSearchResult> result = queryFactory
                    .select(new QMemberSearchResult(member.id.as("memberId"),
                            member.memberIdName,
                            member.password,member.nickName,
                            member.userName, member.phoneNumber,
                            member.authorizationStatus, member.hospitalNumber)
                    )
                    .from(member)
                    .where(
                            member.memberIdName.eq(condition.getAllSearch())
                                    .or(member.nickName.eq(condition.getAllSearch()))
                                    .or(member.userName.eq(condition.getAllSearch()))
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            List<MemberSearchResult> content = result.getResults();
            long total = result.getTotal();

            return new PageImpl<>(content, pageable, total);
        }
        //각자 검색
        else{
            QueryResults<MemberSearchResult> result = queryFactory
                    .select(new QMemberSearchResult(member.id.as("memberId"),
                            member.memberIdName,
                            member.password,member.nickName,
                            member.userName, member.phoneNumber,
                            member.authorizationStatus, member.hospitalNumber)
                    )
                    .from(member)
                    .where(
                            memberIdEq(condition.getMemberId())
                            ,(memberIdNameEq(condition.getMemberIdName()))
                            ,(memberNickNameEq(condition.getNickName()))
                            ,(memberUserNameEq(condition.getUserName()))
                            ,(memberPhoneNumberEq(condition.getPhoneNumber()))
                            ,(memberAuthorizationStatusEq(condition.getAuthorizationStatus()))
                            ,(memberHospitalNumberEq(condition.getHospitalNumber()))
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            List<MemberSearchResult> content = result.getResults();
            long total = result.getTotal();

            return new PageImpl<>(content, pageable, total);
        }

    }

    private BooleanExpression memberIdEq(Long id){
        return id != null? member.id.eq(id): null;
    }
    private BooleanExpression memberIdNameEq(String memberIdName){
        return hasText(memberIdName) ? member.memberIdName.eq(memberIdName): null ;
    }
    private BooleanExpression memberNickNameEq(String nickName){
        return hasText(nickName) ? member.nickName.eq(nickName): null ;
    }
    private BooleanExpression memberUserNameEq(String userName){
        return hasText(userName) ? member.userName.eq(userName): null ;
    }
    private BooleanExpression memberPhoneNumberEq(String phoneNumber){
        return phoneNumber != null? member.phoneNumber.eq(phoneNumber) : null;
    }
    private BooleanExpression memberAuthorizationStatusEq(Authorization authorizationStatus){
        return authorizationStatus != null? member.authorizationStatus.eq(authorizationStatus): null;
    }
    private BooleanExpression memberHospitalNumberEq(Long hospitalNumber){
        return hospitalNumber != null? member.hospitalNumber.eq(hospitalNumber): null;
    }

}
