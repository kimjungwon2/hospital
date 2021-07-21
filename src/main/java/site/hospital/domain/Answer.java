package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qanda_id")
    private QandA qandA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffHosInformation_id")
    private Member member;

    private String answerContent;

    @Builder
    public Answer(String answerContent) {
        this.answerContent = answerContent;
    }

    public void changeMember(Member member){
        this.member = member;
        member.getAnswers().add(this);
    }

    public void changeQandA(QandA qandA){
        this.qandA = qandA;
        qandA.setAnswer(this);
    }

    //생성 메서드
    public static Answer createAnswer(Member member, QandA qandA){
        Answer answer = new Answer();
        answer.changeMember(member);
        answer.changeQandA(qandA);

        return answer;
    }

}
