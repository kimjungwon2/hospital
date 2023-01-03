package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(length = 1000)
    @NotNull
    private String content;


    //== 연관 관계 메서드 ==/
    public void changeMember(Member member) {
        this.member = member;
        member.getQuestions().add(this);
    }

    public void changeHospital(Hospital hospital) {
        this.hospital = hospital;
        hospital.getQuestions().add(this);
    }

    public void changeAnswer(Answer answer) {
        this.answer = answer;
        answer.setQuestion(this);
    }

    @Builder
    public Question(String content) {
        this.content = content;
    }

    //생성 메서드
    public static Question CreateQuestion(Member member, Hospital hospital, String content) {
        Question question = new Question(content);
        question.changeMember(member);
        question.changeHospital(hospital);

        return question;
    }

    //비즈니스 메서드


    public void modifyQandA(String content) {
        this.content = content;
    }
}
