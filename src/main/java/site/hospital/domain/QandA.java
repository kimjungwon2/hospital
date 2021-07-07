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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "qanda_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private String content;

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member){
        this.member = member;
        member.getQandas().add(this);
    }

    public void changeHospital(Hospital hospital){
        this.hospital = hospital;
        hospital.getQandAs().add(this);
    }

    public QandA(String content){
        this.content = content;
    }

    //생성 메서드
    public static QandA CreateQandA(Member member,Hospital hospital, String content){
        QandA qandA = new QandA(content);
        qandA.changeMember(member);
        qandA.changeHospital(hospital);

        return qandA;
    }
}
