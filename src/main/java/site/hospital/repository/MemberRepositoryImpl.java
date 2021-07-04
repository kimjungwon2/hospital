package site.hospital.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;


import site.hospital.domain.Authorization;
import site.hospital.dto.MemberSearchResult;
import site.hospital.dto.MemberSearchCondition;
import site.hospital.dto.QMemberSearchResult;

import static site.hospital.domain.QMember.member;
import static org.springframework.util.StringUtils.hasText;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberSearchResult> search(MemberSearchCondition condition){

        return queryFactory
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
                .fetch();
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
    private BooleanExpression memberPhoneNumberEq(Integer phoneNumber){
        return phoneNumber != null? member.phoneNumber.eq(phoneNumber) : null;
    }
    private BooleanExpression memberAuthorizationStatusEq(Authorization authorizationStatus){
        return authorizationStatus != null? member.authorizationStatus.eq(authorizationStatus): null;
    }
    private BooleanExpression memberHospitalNumberEq(Long hospitalNumber){
        return hospitalNumber != null? member.hospitalNumber.eq(hospitalNumber): null;
    }

}
