package site.hospital.repository.bookmark;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.Bookmark;
import site.hospital.dto.StaffBookmarkSearchCondition;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.QBookmark.bookmark;
import static site.hospital.domain.member.QMember.member;
import static site.hospital.domain.hospital.QHospital.hospital;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //고객의 즐겨찾기 유무 확인
    public Bookmark isUserBookmark(Long memberId, Long hospitalId) {
        Bookmark result = queryFactory
                .select(bookmark)
                .from(bookmark)
                .where(memberIdEq(memberId), hospitalIdEq(hospitalId))
                .fetchOne();

        return result;
    }

    //병원 관계자 북마크 확인.
    public Page<Bookmark> staffSearchBookmark(Long hospitalId,
            StaffBookmarkSearchCondition condition,
            Pageable pageable) {
        QueryResults<Bookmark> result = queryFactory
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

        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public List<Bookmark> searchBookmark(Long memberId, Long hospitalId) {
        List<Bookmark> result = queryFactory
                .select(bookmark)
                .from(bookmark)
                .join(bookmark.member, member).fetchJoin()
                .join(bookmark.hospital, hospital).fetchJoin()
                .where(memberIdEq(memberId), hospitalIdEq(hospitalId))
                .fetch();

        return result;
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
