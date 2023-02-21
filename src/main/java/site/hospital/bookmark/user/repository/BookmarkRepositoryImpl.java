package site.hospital.bookmark.user.repository;


import static site.hospital.bookmark.user.domain.QBookmark.bookmark;
import static site.hospital.hospital.user.domain.QHospital.hospital;
import static site.hospital.member.user.domain.QMember.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.bookmark.user.repository.dto.ManagerBookmarkSearchCondition;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Bookmark userCheckBookmark(Long memberId, Long hospitalId) {
        return queryFactory
                .select(bookmark)
                .from(bookmark)
                .where(memberIdEq(memberId), hospitalIdEq(hospitalId))
                .fetchOne();
    }

    public Page<Bookmark> managerSearchBookmarkUsers(
            Long hospitalId,
            ManagerBookmarkSearchCondition condition,
            Pageable pageable
    ) {
        QueryResults<Bookmark> result =
                queryFactory
                .select(bookmark)
                .from(bookmark)
                .join(bookmark.member, member).fetchJoin()
                .where(hospitalIdEq(hospitalId),
                        memberIdNameLike(condition.getMemberIdName()),
                        nickNameEq(condition.getNickName()),
                        phoneNumberLike(condition.getPhoneNumber())
                )
                .fetchResults();

        List<Bookmark> content = result.getResults();

        long totalPage = result.getTotal();

        return new PageImpl<>(content, pageable, totalPage);
    }

    public List<Bookmark> searchBookmark(Long memberId, Long hospitalId) {
        return queryFactory
                .select(bookmark)
                .from(bookmark)
                .join(bookmark.member, member).fetchJoin()
                .join(bookmark.hospital, hospital).fetchJoin()
                .where(memberIdEq(memberId), hospitalIdEq(hospitalId))
                .fetch();

    }

    private BooleanExpression memberIdNameLike(String memberIdName) {
        return memberIdName == null ? null : member.memberIdName.contains(memberIdName);
    }

    private BooleanExpression nickNameEq(String nickName) {
        return nickName == null ? null : member.nickName.eq(nickName);
    }

    private BooleanExpression phoneNumberLike(String phoneNumber) {
        return phoneNumber == null ? null : member.phoneNumber.contains(phoneNumber);
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? bookmark.member.id.eq(id) : null;
    }

    private BooleanExpression hospitalIdEq(Long id) {
        return id != null ? bookmark.hospital.id.eq(id) : null;
    }

}
