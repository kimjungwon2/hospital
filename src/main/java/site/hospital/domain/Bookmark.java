package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.Member;

import javax.persistence.*;

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
