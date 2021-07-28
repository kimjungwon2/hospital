package site.hospital.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "member_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="authority_id")
    private Authority authority;

    @Builder
    public MemberAuthority(Member member, Authority authority) {
        this.member = member;
        this.authority = authority;
    }

    //권한을 주기
    public void giveAuthority(Authority authority){
        this.authority = authority;
    }

}
