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
    private String nickName;


    @Builder
    public OAuthAttributes(
            Map<String, Object> attributes,
            String nameAttributeKey,
            String email,
            String name,
            String phoneNumber,
            String nickName
    ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
    }

    public static OAuthAttributes of(
            String registrationId,
            String userNameAttributeName,
            Map<String, Object> attributes
    ){
        if(registrationId.equals("naver")){
            return ofNaver("id",attributes);
        } else if(registrationId.equals("kakao")){
            return ofKakao("id",attributes);
        }

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

    private static OAuthAttributes ofNaver(
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .nickName((String) response.get("nickname"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        kakao_account.put("id", attributes.get("id"));

        Map<String, Object> properties = (Map<String,Object>) attributes.get("properties");
        kakao_account.put("nickname", properties.get("nickname"));

        return OAuthAttributes.builder()
                .name((String) properties.get("nickname"))
                .nickName((String) properties.get("nickname"))
                .email((String) kakao_account.get("email"))
                .attributes(kakao_account)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Member toEntity(){

        if(nickName == null) {
            return Member.builder()
                    .memberIdName(email)
                    .nickName(name)
                    .userName(name)
                    .phoneNumber(phoneNumber)
                    .memberStatus(MemberStatus.NORMAL)
                    .build();
        }

        return Member.builder()
                .memberIdName(email)
                .nickName(nickName)
                .userName(name)
                .phoneNumber(phoneNumber)
                .memberStatus(MemberStatus.NORMAL)
                .build();
    }


}
