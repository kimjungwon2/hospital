package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Bookmark;
import site.hospital.domain.Hospital;
import site.hospital.domain.Member;
import site.hospital.repository.BookmarkRepository;
import site.hospital.repository.HospitalRepository;
import site.hospital.repository.MemberRepository;

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
}
