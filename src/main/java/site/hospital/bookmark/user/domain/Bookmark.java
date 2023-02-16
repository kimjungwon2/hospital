package site.hospital.bookmark.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseTimeEntity;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.member.user.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member) {
        this.member = member;
        member.getBookmarks().add(this);
    }

    public void changeHospital(Hospital hospital) {
        this.hospital = hospital;
        hospital.getBookmarks().add(this);
    }

    //생성 메서드
    public static Bookmark createBookmark(Member member, Hospital hospital) {
        Bookmark bookmark = new Bookmark();
        bookmark.changeMember(member);
        bookmark.changeHospital(hospital);

        return bookmark;
    }
}
