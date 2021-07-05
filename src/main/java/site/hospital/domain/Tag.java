package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private long id;

    @OneToMany(mappedBy = "tag")
    private List<PostTag> posttags = new ArrayList<>();

    @Column(unique=true, nullable = false)
    private String name;

    //태그 생성
    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
