package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name= "member_id")
    private Long id;


    @OneToMany(mappedBy = "member")
    private List<Appointment> appointments = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<QandA> qandas = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    //회원 이름
    private String userName;
    //회원 아이디
    private String memberIdName;
    private String password;
    private String nickName;

    private int phoneNumber;

    //가입 날짜
    private LocalDateTime registrationDate;

    //회원 권한 부여 상태. [NORMAL, STAFF, ADMIN]
    @Enumerated(EnumType.STRING)
    private Authorization authorizationStatus;
    //병원 번호
    private Long hospitalNumber;


    public Member(String userName){
        this.userName = userName;
    }

}
