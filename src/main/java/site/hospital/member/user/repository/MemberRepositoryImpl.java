package site.hospital.member.user.repository;

import static site.hospital.domain.member.QAuthority.authority;
import static site.hospital.domain.member.QMember.member;
import static site.hospital.domain.member.QMemberAuthority.memberAuthority;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.member.user.domain.Authorization;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberAuthority;
import site.hospital.member.user.domain.MemberStatus;
import site.hospital.member.admin.repository.dto.AdminMemberSearchCondition;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //권한 찾기
    @Override
    public List<MemberAuthority> memberAuthorities(String memberIdName) {
        return queryFactory
                .select(memberAuthority)
                .from(memberAuthority)
                .join(memberAuthority.member, member)
                .join(memberAuthority.authority, authority).fetchJoin()
                .where(memberIdNameEq(memberIdName))
                .fetch();
    }


    //STAFF 권한 유무 확인권한 찾기
    @Override
    public MemberAuthority findMemberStaffAuthority(Long memberId, Authorization authorization) {
        return queryFactory
                .select(memberAuthority)
                .from(memberAuthority)
                .join(memberAuthority.member, member)
                .join(memberAuthority.authority, authority)
                .where(
                        memberIdEq(memberId),
                        authorizationStatusEq(authorization)
                )
                .fetchOne();
    }


    //권한 삭제.
    @Override
    public void adminDeleteMemberAuthority(Member member) {
        queryFactory.delete(memberAuthority)
                .where(memberEq(member))
                .execute();
    }


    @Override
    public Page<Member> adminSearchMembers(AdminMemberSearchCondition condition,
            Pageable pageable) {

        //모두 검색(아이디+닉네임+유저이름+폰번호)
        if (condition.getAllSearch() != null) {
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
        else {
            QueryResults<Member> result = queryFactory
                    .select(member)
                    .from(member)
                    .where(
                            memberIdEq(condition.getMemberId())
                            , (memberIdNameLike(condition.getMemberIdName()))
                            , (memberNickNameEq(condition.getNickName()))
                            , (memberUserNameEq(condition.getUserName()))
                            , (memberPhoneNumberEq(condition.getPhoneNumber()))
                            , (memberHospitalNumberEq(condition.getHospitalNumber()))
                            , (memberStatusEq(condition.getMemberStatus()))
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            List<Member> content = result.getResults();
            long total = result.getTotal();

            return new PageImpl<>(content, pageable, total);
        }

    }

    private BooleanExpression memberEq(Member member) {
        return member == null ? null : memberAuthority.member.eq(member);
    }

    private BooleanExpression memberIdNameEq(String memberIdName) {
        return memberIdName == null ? null : member.memberIdName.eq(memberIdName);
    }

    private BooleanExpression authorizationStatusEq(Authorization authorization) {
        return authorization == null ? null : authority.authorizationStatus.eq(authorization);
    }

    private BooleanExpression memberIdEq(Long id) {
        return id == null ? null : member.id.eq(id);
    }

    private BooleanExpression memberIdNameLike(String memberIdName) {
        return memberIdName == null ? null : member.memberIdName.contains(memberIdName);
    }

    private BooleanExpression memberNickNameEq(String nickName) {
        return nickName == null ? null : member.nickName.eq(nickName);
    }

    private BooleanExpression memberUserNameEq(String userName) {
        return userName == null ? null : member.userName.eq(userName);
    }

    private BooleanExpression memberPhoneNumberEq(String phoneNumber) {
        return phoneNumber == null ? null : member.phoneNumber.eq(phoneNumber);
    }

    private BooleanExpression memberHospitalNumberEq(Long hospitalNumber) {
        return hospitalNumber == null ? null : member.hospitalNumber.eq(hospitalNumber);
    }

    private BooleanExpression memberStatusEq(MemberStatus memberStatus) {
        return memberStatus == null ? null : member.memberStatus.eq(memberStatus);
    }

}
