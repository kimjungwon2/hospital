package site.hospital.answer.manager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseTimeEntity;
import site.hospital.member.user.domain.Member;
import site.hospital.question.user.domain.Question;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private long id;

    @OneToOne(mappedBy = "answer", fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 1000)
    @NotNull
    private String answerContent;


    //연관 관계 메서드
    public void setQuestion(Question question) {
        this.question = question;
    }

    public void changeMember(Member member) {
        this.member = member;
        member.getAnswers().add(this);
    }

    @Builder
    public Answer(String answerContent) {
        this.answerContent = answerContent;
    }

}
