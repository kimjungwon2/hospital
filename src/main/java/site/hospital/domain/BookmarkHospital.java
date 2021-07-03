package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkHospital extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "bookmark_hospital_id")
    private long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

}
