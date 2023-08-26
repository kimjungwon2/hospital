package site.hospital.common.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberStatus;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;
    private String name;
    private String phoneNumber;

    @Builder
    public OAuthAttributes(
            Map<String, Object> attributes,
            String nameAttributeKey,
            String email,
            String name,
            String phoneNumber
    ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public static OAuthAttributes of(
            String registrationId,
            String userNameAttributeName,
            Map<String, Object> attributes
    ){
        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(
            String userNameAttributeName,
            Map<String, Object> attributes
    ){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String)attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .memberIdName(email)
                .nickName(name)
                .userName(name)
                .phoneNumber(phoneNumber)
                .memberStatus(MemberStatus.NORMAL)
                .build();

    }


}
