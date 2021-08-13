package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private long id;

    @OneToOne(mappedBy = "answer", fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String answerContent;


    public void setQuestion(Question question){
        this.question =question;
    }

    @Builder
    public Answer(String answerContent) {
        this.answerContent = answerContent;
    }

    //연관 관계 메서드
    public void changeMember(Member member){
        this.member = member;
        member.getAnswers().add(this);
    }


}
