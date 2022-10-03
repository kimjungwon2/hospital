# :pushpin: 병원 조회 웹사이트
>병원 정보, 위치(지도), 리뷰, Q&A 등의 기능이 있는 웹사이트.

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

## 3. ERD 설계
![병원 ERD2](https://user-images.githubusercontent.com/40010165/193455534-62b45df4-e83e-4820-b37d-32251308f8a6.png)
</br>

## 4. 핵심 기능
</br>

## 5. 트러블슈팅
</br>

## 6. 고려한 점
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
- fetch join으로 해당 객체의 칼럼들을 select를 다 가져오게끔 했습니다. 이러면 LAZY.LOADING으로 인한 조회 성능이 최적화됩니다.
</div>
</details>

<details>
<summary>일대다 관계에서 컬렉션이 있는 칼럼을 조회할 때,  다대일 관계만 모두 페치조인으로 하고. 컬렉션은 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size 적용했습니다.</summary>
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


## 7. 회고&느낀점
</br>
