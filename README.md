# :pushpin: 병원 조회 웹사이트
>RESTful API 방식. 병원 정보, 위치(지도), 리뷰, Q&A 등의 기능이 있는 웹사이트.</br>
>[경기데이터드림](https://data.gg.go.kr/portal/mainPage.do)의 DB를 활용하여 남양주시와 의정부시의 병원 정보를 기입했습니다. (약 200개 가량의 데이터) 

</br>

## 1. 제작 기간 & 참여 인원
- 4개월
- 개인 프로젝트

</br>

## 2. 사용한 기술 스택
#### `Back-end`
  - Java 8
  - Spring Boot 2.5.2
  - Gradle
  - JPA
  - Spring Data JPA
  - QueryDSL 1.0.10
  - Spring Security 
  - JWT 0.11.2
#### `DevOps`
  - MySQL 5.7
  - AWS S3 2.0.1
  - AWS CDN
  - AWS Lambda
 #### `Front-end`
  - HTML
  - CSS
  - Vue.js 2.6.14
</br>

## 3. 프로젝트 구조
### 3.1. Back-end :mag_right: [구조 확인](https://github.com/kimjungwon2/hospital/tree/4d39e3c12ba04a1de79a0574a1c49897216eaf11/src/main/java/site/hospital)
<details>
<summary>패키지 설명</summary>
<div markdown="1">

- **api**: Controller 계층(애플리케이션).
- **configuration**: AWS S3, MultiPart, springBoot Security, AuditorAware 설정.
- **domain**: domain 계층.
- **dto**: 기존의 도메인을 변형. 계층 간 데이터 교환 역할. 모든 계층에서 사용된다.
- **exception**: Exception 처리.
- **jwtToken**: JWT 관리.
- **repository**: repository 계층.
- **service**: Service 계층.
</div>
</details>

<details>
<summary>domain & repository 안의 폴더 의미</summary>
<div markdown="1">

- **domain**: 폴더 이름의 객체와 객체 안의 enum, 임베디드 타입(내장 객체)들을 묶은 것입니다.
- **repository**: 그 엔티티에 QueryDSL을 사용하거나, 복잡한 데이터 조회나 성능을 위해서. @QueryProjection을 사용하여 전체 데이터가 아닌 원하는 데이터만 select 하게끔 QueryDSL을 통해 Query문을 만들었습니다.
</div>
</details>


### 3.2. Front-end :mag_right: [구조 확인](https://github.com/kimjungwon2/hospital/tree/4d39e3c12ba04a1de79a0574a1c49897216eaf11/src/frontend/src)
<details>
<summary>패키지 설명</summary>
<div markdown="1">

- **api**: Axios를 통해 서버에 데이터 요청. 
- **assets**: 사이트에 사용되는 기본 이미지들을 모았습니다.
- **components**: 페이지들의 구성요소인 컴포넌트들을 보관.
- **css**: 공통적으로 사용되는 css입니다.
- **routes**: 각각의 url과 vue의 페이지들을 매칭시켜주는 역할.
- **store**: vue의 store 관련 함수.
- **utils**: 쿠키, 필터(숫자를 받아 날짜로 변경해주는), 이메일 문자열인지 검증해주는 코드들.
- **views**: 각각의 페이지들을 모아둔 파일.
</div>
</details>

## 4. ERD 설계
![병원 ERD2](https://user-images.githubusercontent.com/40010165/193455534-62b45df4-e83e-4820-b37d-32251308f8a6.png)
<details>
<summary><b>도메인 설계 펼치기</b></summary>
<div markdown="1">

### 4.1. 다대다 관계
- 실무에서는 다대다 관계는 너무 복잡해서 사용하지 않는 걸로 들었습니다. 그래서 다대다 관계를 일대다-다대일 관계로 표현했습니다.  
- 아래는 다대다 관계를 어떻게 설정했는지에 관한 예시입니다.
![설명](https://user-images.githubusercontent.com/40010165/193619475-3574b4c5-1ef9-41cc-8f12-78c15f380d48.png)
![설명2](https://user-images.githubusercontent.com/40010165/193619543-bc61ad47-c8bf-4349-a094-c36b60f65d35.png)

### 4.2. 연관관계의 주인 :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/Question.java#L27)
- 일대다 or 다대일 양방향 관계일 경우, 연관관계의 주인을 정해야합니다. 
- 다 관계쪽에 있거나, 외래키가 있는 곳을 연관관계의 주인으로 설정했습니다. 
- 연관관계 주인만이 외래키를 관리(update,create,delete)할 수 있습니다. 
- 연관관계의 주인은 mappedBy속성을 사용하지 않기에, mappedBy의 반대쪽 객체가 연관관계의 주인이 됩니다.

### 4.3. 기본 키(PK)는 Long 타입의 대리 키로 설정
- 모든 Entity의 PK는 Long 타입의 Auto_increment를 사용했습니다. 
- PK를 자연 키(주민번호, 전화번호 등)로 하면 비즈니스 환경이 변할 때, 간혹 기본 키로 인해 수정할 부분이 많아질 경우가 있습니다. 반면 대리 키는 비즈니스와 아무 관련이 없기에 비즈니스가 변경되어도 유연한 대처가 가능합니다. 
- 테이블간의 관계를 설계할 때, 비식별 관계(부모 테이블의 기본 키를 받아 자식 테이블의 외래 키로만 사용)에서 대리 키를 주로 사용합니다. 이러면 매핑도 쉽고 코드가 단순해집니다.

### 4.4. 모든 컬렉션은 필드에서 초기화. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/member/Member.java#L26)
- 필드 레벨에서 생성하는 것이 가장 안전하고, 코드가 간결해집니다. 무엇보다 null 문제에서 안전해집니다.
</div>
</details>
</br>

## 5. 핵심 기능
![계정 종류](https://user-images.githubusercontent.com/40010165/193796762-3770daf3-15ab-4cc0-965f-b5e475de4101.png)
이 서비스는 세 가지 종류의 계정이 있습니다. 
- **사용자**: 병원 정보를 조회하거나, 해당 병원에 리뷰 또는 질문을 등록할 수 있습니다.
- **병원 관계자**: 자신의 병원만 관리할 수 있습니다. 사진을 올리거나, 병원 정보 수정, 태그 설정, 사용자가 등록한 질문에 답변을 할 수 있습니다. 
- **관리자**: 위의 모든 기능을 할 수 있습니다. 계정을 관리하거나, 병원 정보를 관리. 무엇보다 사용자가 등록한 리뷰의 영수증을 확인해서, 이 리뷰가 검증된 것임을 승인할 수 있습니다. 

</br>
<details>
<summary><b>핵심 기능 설명 펼치기</b></summary>
<div markdown="1">

### 5.1. Repository 계층

### 5.2. 병원 검색 

### 5.3. 세 가지의 계정 종류

### 5.4. 이미지 관리 :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/service/ImageManagementService.java)
#### 5.4.1. Stateful vs Stateless
Stateful
-------------
나중에 서비스가 커질 때, 서버를 확장해야할 때가 있습니다. 이럴 때는 아래의 방식처럼 서버를 확장해야 합니다.
![Stateful](https://user-images.githubusercontent.com/40010165/193820645-540b0ebc-c135-48aa-9dbe-474213c45f4f.png)</br>
Load Balancer는 요청이 들어오면 서버의 부하가 없는 곳에 전해줍니다. 이럴 경우 아래의 사건들이 발생합니다.
- **DB**: 서버와 완전 무관합니다. 처리를 하고 호출만 해줘서, 중앙화된 서버가 있어서 똑같은 데이터를 바라보기에 상관이 없습니다.
- **이미지**: 아미지 파일들이 흩어져서 저장됩니다. 즉, 서버들이 상태를 갖게 됩니다. Stateful한 상태가 되면 아래의 세 가지 문제가 발생합니다.
  - **이미지 삭제**: 생성과 조회는 괜찮을 수 있어도, 삭제의 경우 문제가 발생합니다. 예를 들어 img6을 Server1에서 삭제할라고 하면, 삭제를 못 합니다.
  - **서버 축소**: 항상 서버가 3대만 있는 게 아닙니다. 부하가 줄어드면 자동으로 서버 수를 줄이게되는데, 이러면 이미지도 같이 삭제됩니다.
  - **성능적인 측면**: 이미지를 관리하는걸 서버를 통해서 하면, 메인 서버가 다른 작업들을 하는데 퍼포먼스 상으로 영향을 줘서 성능에 영향을 줄 수 있다.

Stateless
-------------
![Stateless ](https://user-images.githubusercontent.com/40010165/193825523-4d59f86a-31cb-45ac-a888-4ceda3f0c4fd.png)</br>
저는 위의 문제 때문에 AWS S3를 사용했습니다. S3를 사용하면 아래와 같은 결과가 발생합니다.
- S3 저장소를 별도로 둬서, Stateless한 상태가 됩니다.
- 이미지뿐만 아니라 영상, 파일도 더 이상 서버에 저장하지 않고, S3에 저장할 수 있습니다.
- Stateless한 상태가 돼서 서버의 확장성이 좋아집니다.


#### 5.4.2. 이미지 로딩 속도 높이기
- CDN 사용으로 캐시 서버를 통해 최적화됩니다. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/frontend/src/views/ViewHospitalPage.vue#L9)
- 로딩이 빠르려면 이미지 원본 크기를 줄여야합니다. 저는 서버의 부하를 줄이기 위해 AWS Lamda를 사용해서, width 길이를 140 & 600으로 리사이징 했습니다. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/frontend/lambda/index.js)

</div>
</details>

</br>

## 6. 트러블슈팅
</br>

## 7. 고려한 점
<details>
<summary>모든 연관 관계는 지연 로딩(LazyLoading)으로 설정했습니다. </summary>
<div markdown="1">

- 즉시 로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵기 때문입니다.
- XToOne(일대일, 다대일) 관계는 기본이 EAGER Loading이라서 직접 지연로딩으로 설정했습니다. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/Bookmark.java#L21)
</div>
</details>

<details>
<summary>Entity 클래스에서 Setter 메소드를 만들지 않았습니다.(연관관계 메서드 제외)</summary>
<div markdown="1">

- **이유**: 클래스의 인스턴스 값들이 언제 어디서 변하는지 코드상으로 명확하게 구분할 수 없어, 차후 기능 변경 시 Setter를 사용하면 정말 복잡해집니다.
- **해결**: 기본적인 구조는 생성자를 통해 DB에 삽입합니다. 값 변경이 필요한 경우 이벤트에 맞는 public 메소드를 호출하여 변경했습니다.
- **예시**

<b>Setter 사용</b>
```
  Estimation estimation = new Estimation();
  Member.setEstimationList("주사제");
  Member.setEstimationGrade("1등급");
```
*****
<b>생성자를 통해 변경</b> :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/4d39e3c12ba04a1de79a0574a1c49897216eaf11/src/main/java/site/hospital/domain/estimation/Estimation.java#L44)
```
  public void modifyEstimation(Estimation estimation){
        this.estimationList = estimation.getEstimationList();
        this.distinctionGrade = estimation.getDistinctionGrade();
    }
```
이렇게 modifyEstimation 함수명으로 **정보를 수정한다는 걸 한 눈에 알 수 있습니다**. 

</div>
</details>

<details>
<summary>양방향 연관관계 메서드를 entity 양쪽 객체에서 둘 다 작성하는 게 아닌 한쪽만 작성했습니다. </summary>
<div markdown="1">

- :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/hospital/Hospital.java#L71)
- 기존의 개발자가 작성한 두 개의 연관관계 메서드 중에서 코드를 작성하는 다른 개발자들은 어떤 메서드를 호출해야 할지 혼란스러움을 느끼기 때문입니다.
</div>
</details>

<details>
<summary>엔티티나 임베디드 타입은 기본 생성자를 protected로 설정했습니다. 저는 @NoArgsConstructor(access = AccessLevel.PROTECTED) 롬복으로 대체했습니다.</summary>
<div markdown="1">

- Setter로 타인이 무분별하게 값을 변경하는 걸 방지하기 위해, protected 생성자로 아무데나 생성되는 걸 제약한다.
- **private을 사용 못하는 이유**: JPA 표준 스펙에 디폴트 생성자가 있어야합니다. JPA가 프록시 기술을 쓸 때,  jpa hibernate가 객체를 강제로 만들어야하는데 private로 만들면 이것이 다 막힙니다.
</div>
</details>

<details>
<summary>Service 계층에서 @Transaction(readOnly = true) 사용.</summary>
<div markdown="1">

- 읽기 모드로 성능을 최적화하기 위해서입니다.
</div>
</details>

<details>
<summary>데이터를 수정할 때, merge보다는 dirty checking(변경 감지)를 이용해 업데이트.</summary>
<div markdown="1">

- merge 사용 시, 값이 없으면 null로 업데이트 되기에 변경 감지를 사용했습니다.
</div>
</details>

<details>
<summary>Auditing으로 등록일, 수정일을 다 넣었습니다. </summary> 
<div markdown="1">

- :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/domain/baseEntity/BaseEntity.java)
- 데이터를 언제 바꿨냐, 언제 문제가 생겼냐는 게 중요해서 넣으면 운영할 때 편합니다.
- 등록, 수정 두가지는 모든 테이블에 다 적용했습니다.
</div>
</details>

<details>
<summary>API 계층에서 Entity를 그대로 파라미터에 넣거나, Entity 값으로 반환하지 않았습니다. 대신 API 스펙을 위한 별도의 DTO를 생성했습니다.</summary>
<div markdown="1">

- :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/api/HospitalApiController.java#L165)
- Entity를 웹에 노출하면 api 스펙이 변해버리거나 패스워드가 그대로 노출되기에, DTO로 변환해줘야 합니다.
</div>
</details>

<details>
<summary>양방향 연관 관계에서 객체(다른 객체가 칼럼에 있는)들을 조회 시, 엔티티를 DTO로 변환하고 Repository에서 fetch join 사용했습니다.</summary>
<div markdown="1">

- :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/repository/hospital/HospitalRepositoryImpl.java#L22)
- fetch join으로 해당 객체의 칼럼들을 select를 다 가져오게끔 했습니다. 이러면 LAZY.LOADING과 조회 성능이 최적화됩니다.
</div>
</details>

<details>
<summary>일대다 관계에서 컬렉션이 있는 칼럼을 조회할 때,  XToOne(일대일, 다대일) 관계만 모두 페치조인으로 하고. 컬렉션은 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size 적용했습니다.</summary>
<div markdown="1">

- :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/resources/application.yml#L18)
- 이러면 성능 최적화도 되고,  페이징도 적용됩니다. 쿼리 호출 수가 1 + N  => 1 + 1 로 최적화 돼서 조인보다 DB 데이터 전송량이 최적화 됩니다.
- fetch join 방식과 비교해서 쿼리 호출 수가 약간 증가하지만, DB 데이터 전송량이 감소합니다.
</div>
</details>

<details>
<summary>페이징 시 countQuery를 따로 분리했습니다.</summary>
<div markdown="1">

- :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/repository/hospital/searchQuery/HospitalSearchRepository.java#L100)
- 분리함으로써 성능을 최적화했습니다.
</div>
</details>

</br>


## 8. 회고&느낀점
</br>
