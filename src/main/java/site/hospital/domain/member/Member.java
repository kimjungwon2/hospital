package site.hospital.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.*;
import site.hospital.domain.appointment.Appointment;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.review.Review;
import site.hospital.domain.reviewLike.ReviewLike;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "member_id")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Appointment> appointments = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Answer> answers = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    //회원 아이디
    @Column(unique=true, nullable = false)
    private String memberIdName;
    private String password;
    private String nickName;
    //회원 이름
    private String userName;
    private String phoneNumber;

    //병원 번호
    private Long hospitalNumber;

    /*
        생성자
    */
    //회원 생성
    @Builder
    public Member(String memberIdName, String password, String userName, String nickName,
                  String phoneNumber){
        this.memberIdName = memberIdName;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    //수정하기
    public void modifyMember(Member member){
        this.nickName =member.nickName;
        this.phoneNumber=member.phoneNumber;
        this.userName = member.userName;
    }

}
