package site.hospital.member.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.answer.user.domain.Answer;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.question.user.domain.Question;
import site.hospital.review.user.domain.ReviewLike;
import site.hospital.common.domain.BaseTimeEntity;
import site.hospital.review.user.domain.Review;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

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
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberAuthority> memberAuthorities = new ArrayList<>();

    //회원 아이디
    @Column(unique = true, nullable = false)
    @NotNull
    private String memberIdName;

    private String password;
    @NotNull
    private String nickName;
    @NotNull
    private String userName;

    private String phoneNumber;


    private Long hospitalNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    MemberStatus memberStatus;


    @Builder
    public Member(
            String memberIdName,
            String password,
            String userName,
            String nickName,
            String phoneNumber,
            MemberStatus memberStatus,
            Long hospitalNumber
    ) {
        this.memberIdName = memberIdName;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.memberStatus = memberStatus;

        if (memberStatus == MemberStatus.STAFF) {
            this.hospitalNumber = hospitalNumber;
        }
    }

    public void adminModifyMember(Member member) {
        this.nickName = member.getNickName();
        this.phoneNumber = member.getPhoneNumber();
        this.userName = member.getUserName();
        this.memberStatus = member.getMemberStatus();

        if (member.memberStatus == MemberStatus.STAFF) {
            this.hospitalNumber = member.getHospitalNumber();
        }
    }

    public void modifyMember(Member member) {
        this.nickName = member.getNickName();
        this.phoneNumber = member.getPhoneNumber();
        this.userName = member.getUserName();
    }

    public Member updateOauth(String name, String phoneNumber){
        this.nickName = name;
        this.phoneNumber = phoneNumber;
        this.userName = name;

        return this;
    }
}
