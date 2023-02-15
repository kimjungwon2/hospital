package site.hospital.tag.domain;

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
import site.hospital.common.domain.BaseEntity;
import site.hospital.hospital.user.domain.Hospital;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postTag_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    //생성 메서드
    public static PostTag createPostTag(Tag tag, Hospital hospital) {
        PostTag postTag = new PostTag();
        postTag.changeTag(tag);
        postTag.changeHospital(hospital);

        return postTag;
    }

    //== 연관 관계 메서드 ==/
    public void changeTag(Tag tag) {
        this.tag = tag;
        tag.getPostTags().add(this);
    }

    public void changeHospital(Hospital hospital) {
        this.hospital = hospital;
        hospital.getPostTags().add(this);
    }

}
