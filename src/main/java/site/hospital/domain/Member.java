package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "member_id")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Appointment> appointments = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<QandA> qandas = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    //회원 아이디
    @Column(unique=true)
    private String memberIdName;
    private String password;
    private String nickName;
    //회원 이름
    private String userName;
    private int phoneNumber;

    //회원 권한 부여 상태. [NORMAL, STAFF, ADMIN]
    @Enumerated(EnumType.STRING)
    private Authorization authorizationStatus;
    //병원 번호
    private Long hospitalNumber;


    /*
        생성자
    */

    //회원 생성
    @Builder
    public Member(String memberIdName, String password, String userName, String nickName,
                             int phoneNumber){
        this.memberIdName = memberIdName;
        this.password = password;
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.authorizationStatus = authorizationStatus.NORMAL;
    }

}
