package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Bookmark;
import site.hospital.domain.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.repository.bookmark.BookmarkRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.member.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional
    public Long bookmark(Long memberId, Long hospitalId){
        Member member = memberRepository.findById(memberId).orElse(null);
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        Bookmark bookmark = Bookmark.createBookmark(member,hospital);
        bookmarkRepository.save(bookmark);

        return bookmark.getId();
    }

    public List<Bookmark> searchAdminBookmark(){
        List<Bookmark> Bookmark = bookmarkRepository.searchBookmark(null,null);

        return Bookmark;
    }
    //멤버 예약 조회
    public List<Bookmark> searchMemberBookmark(Long memberId){
        List<Bookmark> Bookmark = bookmarkRepository.searchBookmark(memberId,null);

        return Bookmark;
    }
    //병원 예약 조회
    public List<Bookmark> searchHospitalBookmark(Long hospitalId){
        List<Bookmark> Bookmark = bookmarkRepository.searchBookmark(null, hospitalId);

        return Bookmark;
    }
}
