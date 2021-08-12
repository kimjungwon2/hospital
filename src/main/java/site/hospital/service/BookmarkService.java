package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Bookmark;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.dto.StaffBookmarkSearchCondition;
import site.hospital.repository.bookmark.BookmarkRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.member.MemberRepository;

import javax.servlet.ServletRequest;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final JwtStaffAccessService jwtStaffAccessService;

    //북마크 여부 확인.
    public Bookmark isBookmark(Long memberId, Long hospitalId){
        Bookmark isBookmark = bookmarkRepository.isUserBookmark(memberId,hospitalId);

        return isBookmark;
    }

    @Transactional
    public void bookmark(Long memberId, Long hospitalId){

        //북마크 했는지 체크 여부
        Boolean existence = false;

        //북마크 있는지 확인.
        Bookmark isBookmark = bookmarkRepository.isUserBookmark(memberId,hospitalId);

        //북마크 존재 시, true
        if(isBookmark!=null) existence = true;

        //북마크가 있으면 삭제.
        if(existence== true) bookmarkRepository.delete(isBookmark);

        //북마크 여부가 없으면 북마크 저장
        else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));
            Bookmark bookmark = Bookmark.createBookmark(member, hospital);
            bookmarkRepository.save(bookmark);
        }
    }

    //병원 관계자 즐겨찾기 검색
    public Page<Bookmark> staffSearchBookmarkUsers(ServletRequest servletRequest, StaffBookmarkSearchCondition condition, Pageable pageable){
        Long JwtHospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);

        return bookmarkRepository.staffSearchBookmark(JwtHospitalId, condition, pageable);
    }

    //관리자 즐겨찾기 조회
    public List<Bookmark> searchAdminBookmark(){
        List<Bookmark> Bookmark = bookmarkRepository.searchBookmark(null,null);

        return Bookmark;
    }

    //멤버 즐겨찾기 조회
    public List<Bookmark> searchMemberBookmark(Long memberId){
        List<Bookmark> Bookmark = bookmarkRepository.searchBookmark(memberId,null);

        return Bookmark;
    }
}
