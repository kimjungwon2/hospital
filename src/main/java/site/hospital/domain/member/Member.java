package site.hospital.domain.member;

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
import site.hospital.domain.Answer;
import site.hospital.domain.Bookmark;
import site.hospital.domain.Question;
import site.hospital.domain.ReviewLike;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.review.Review;

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
    @NotNull
    private String password;
    @NotNull
    private String nickName;
    //회원 이름
    @NotNull
    private String userName;
    @NotNull
    private String phoneNumber;

    //병원 번호
    private Long hospitalNumber;

    //멤버 권한.
    @Enumerated(EnumType.STRING)
    @NotNull
    MemberStatus memberStatus;

    /*
        생성자
    */
    //회원 생성
    @Builder
    public Member(String memberIdName, String password, String userName, String nickName,
            String phoneNumber, MemberStatus memberStatus, Long hospitalNumber) {
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

    //수정하기
    public void adminModifyMember(Member member) {
        this.nickName = member.getNickName();
        this.phoneNumber = member.getPhoneNumber();
        this.userName = member.getUserName();
        this.memberStatus = member.getMemberStatus();

        if (member.memberStatus == MemberStatus.STAFF) {
            this.hospitalNumber = member.getHospitalNumber();
        }
    }

    //수정하기
    public void modifyMember(Member member) {
        this.nickName = member.getNickName();
        this.phoneNumber = member.getPhoneNumber();
        this.userName = member.getUserName();
    }

}
