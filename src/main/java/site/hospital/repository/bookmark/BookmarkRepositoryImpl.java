package site.hospital.repository.bookmark;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hospital.domain.Bookmark;

import javax.persistence.EntityManager;
import java.util.List;

import static site.hospital.domain.QBookmark.bookmark;
import static site.hospital.domain.member.QMember.member;
import static site.hospital.domain.QHospital.hospital;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    //고객의 즐겨찾기 유무 확인
    public Bookmark isUserBookmark(Long memberId, Long hospitalId){
        Bookmark result = queryFactory
                .select(bookmark)
                .from(bookmark)
                .where(memberIdEq(memberId),hospitalIdEq(hospitalId))
                .fetchOne();

        return result;
    }

    public List<Bookmark> searchBookmark(Long memberId,Long hospitalId){
        List<Bookmark> result = queryFactory
                .select(bookmark)
                .from(bookmark)
                .join(bookmark.member, member).fetchJoin()
                .join(bookmark.hospital, hospital).fetchJoin()
                .where(memberIdEq(memberId),hospitalIdEq(hospitalId))
                .fetch();

        return result;
    }

    private BooleanExpression memberIdEq(Long id){
        return id != null? bookmark.member.id.eq(id): null;
    }
    private BooleanExpression hospitalIdEq(Long id){
        return id != null? bookmark.hospital.id.eq(id): null;
    }

}
