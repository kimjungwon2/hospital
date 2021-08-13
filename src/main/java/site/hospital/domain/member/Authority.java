package site.hospital.domain.member;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "authority_id")
    private Long id;

    //회원 권한 부여 상태. [USER, MANAGER, ADMIN]
    @Column(unique=true, nullable = false)
    @Enumerated(EnumType.STRING)
    private Authorization authorizationStatus;

    public Authority(Authorization authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }
}
