package site.hospital.tag.manager.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tag")
    private final List<PostTag> postTags = new ArrayList<>();

    @Column(unique = true, nullable = false)
    private String name;

    //태그 생성
    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
