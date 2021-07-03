package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QandA extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name= "qanda_id")
    private long id;

    @OneToMany(mappedBy="qanda")
    private List<QandAHospital> qandAHospitals= new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String hospitalName;
    private String content;

}
