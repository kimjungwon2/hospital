package site.hospital.domain.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "authority_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    //병원 번호
    private long hospitalNo;

    @Builder
    public MemberAuthority(Member member, Authority authority, long hospitalNo) {
        this.member = member;
        this.authority = authority;
        this.hospitalNo = hospitalNo;
    }

    public void giveHospitalNumber(Long hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    //권한을 주기
    public void giveAuthority(Authority authority) {
        this.authority = authority;
    }

}
