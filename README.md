# :pushpin: 병원 조회 웹사이트
>병원 정보, 위치(지도), 리뷰, Q&A 등의 기능이 있는 웹사이트. </br>
>[경기데이터드림](https://data.gg.go.kr/portal/mainPage.do)의 DB를 활용하여 남양주시와 의정부시의 병원 정보를 기재했습니다.</br> 
>사이트 주소: http://3.37.47.173/ 
<br>

- **사용 방법**: 검색 창에 '**내과**'를 치시면, 병원 목록들을 보실 수 있습니다. '**센텀**'으로 검색하시면 병원 관리자가 등록된 병원을 볼 수 있습니다. 병원 관리자가 등록된 병원은 상세 정보와 Q&A 정보들을 볼 수 있습니다. 

</br>

- **병원 관계자(엘병원) 계정**: ID = manager53@naver.com. PW = aoslwj5. </br>
- **관리자 계정**: 악용을 방지하기 위해서 mac20010@naver.com 메일로 요청하시면 아이디와 패스워드를 제공하겠습니다.</br>

## 1. 프로젝트 요약 
![개발자](https://github.com/kimjungwon2/hospital/assets/40010165/2faf41f0-0b1e-4ba2-b7f2-3c050bbc986c)
</br>

## 2. 프로젝트 구조
### Architecture
![architecture](https://user-images.githubusercontent.com/40010165/216532852-b428aa72-c4da-4ad1-aca3-93b17f6b73c6.png)

### CI/CD
![CICD](https://user-images.githubusercontent.com/40010165/234750622-304cb55f-e9af-4a0c-b9fb-c7cb53069673.png)
![sonarQube 적용](https://user-images.githubusercontent.com/40010165/222424257-fcaebbf9-e630-4ec1-8574-259af083a13c.png)

## 3. ERD 설계
![ERD](https://user-images.githubusercontent.com/40010165/194764130-5e4caaff-892f-47a0-a871-78929755aeb7.png)
<details>
<summary><b>도메인 설계 펼치기</b></summary>
<div markdown="1">

### 3.1. 다대다 관계
- 실무에서는 다대다 관계는 너무 복잡해서 사용하지 않는 걸로 들었습니다. 그래서 다대다 관계를 일대다-다대일 관계로 표현했습니다.  
  
- 아래는 다대다 관계를 어떻게 설정했는지에 관한 예시입니다.
![설명](https://user-images.githubusercontent.com/40010165/193619475-3574b4c5-1ef9-41cc-8f12-78c15f380d48.png)
![설명2](https://user-images.githubusercontent.com/40010165/193619543-bc61ad47-c8bf-4349-a094-c36b60f65d35.png)

### 3.2. 연관관계의 주인 :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/Question.java#L27)
- 일대다 or 다대일 양방향 관계일 경우, 연관관계의 주인을 정해야 합니다. 
  
- 다 관계 쪽에 있거나, 외래키가 있는 곳을 연관관계의 주인으로 설정했습니다. 
  
- 연관관계 주인만이 외래키를 관리(update,create,delete)할 수 있습니다. 
  
- 연관관계의 주인은 mappedBy속성을 사용하지 않기에, mappedBy의 반대쪽 객체가 연관관계의 주인이 됩니다.


### 3.3. 기본 키(PK)는 Long 타입의 대리 키로 설정
- 모든 Entity의 PK는 Long 타입의 Auto_increment를 사용했습니다. 
  
- PK를 자연 키(주민등록번호, 전화번호 등)로 하면 비즈니스 환경이 변할 때, 간혹 기본 키로 인해 수정할 부분이 많아질 경우가 있습니다. 반면 대리 키는 비즈니스와 아무 관련이 없기에 비즈니스가 변경되어도 유연한 대처가 가능합니다. 
  
- 테이블 간의 관계를 설계할 때, 비식별 관계(부모 테이블의 기본 키를 받아 자식 테이블의 외래 키로만 사용)에서 대리 키를 주로 사용합니다. 이러면 매핑도 쉽고 코드가 단순해집니다.

  
### 3.4. 모든 컬렉션은 필드에서 초기화. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/member/Member.java#L26)
- 필드 레벨에서 생성하는 것이 가장 안전하고, 코드가 간결해집니다. 무엇보다 null 문제에서 안전해집니다.

  
### 3.5. 모든 연관 관계는 지연 로딩(LazyLoading)으로 설정. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/Bookmark.java#L21)
- 즉시 로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵기 때문입니다.

- XToOne(일대일, 다대일) 관계는 기본이 EAGER Loading이라서 직접 지연 로딩으로 설정했습니다.

  
### 3.6. 양방향 연관관계 메서드를 entity 양쪽 객체에서 둘 다 작성하는 게 아닌 한쪽만 작성. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/hospital/Hospital.java#L71)
- 기존의 개발자가 작성한 두 개의 연관관계 메서드 중에서 코드를 작성하는 다른 개발자들은 어떤 메서드를 호출해야 할지 혼란스러움을 느끼기 때문입니다.

</div>
</details>
</br>

## 4. 핵심 기능
### 4.1. 계정 권한
![계정 종류](https://user-images.githubusercontent.com/40010165/193796762-3770daf3-15ab-4cc0-965f-b5e475de4101.png)
이 서비스는 세 가지 종류의 계정이 있습니다. 
- **사용자**: 병원 정보를 조회하거나, 해당 병원에 리뷰 또는 질문을 등록할 수 있습니다.

- **병원 관계자**: 자신의 병원만 관리할 수 있습니다. 사진을 올리거나, 병원 정보 수정, 태그 설정, 사용자가 등록한 질문에 답변을 할 수 있습니다. 

- **관리자**: 위의 모든 기능을 할 수 있습니다. 계정을 관리하거나, 병원 정보를 관리. 무엇보다 사용자가 등록한 리뷰의 영수증을 확인해서, 이 리뷰가 검증된 것임을 승인할 수 있습니다. 
</br>
