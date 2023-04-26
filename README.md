# :pushpin: 병원 조회 웹사이트
>병원 정보, 위치(지도), 리뷰, Q&A 등의 기능이 있는 웹사이트. </br>
>[경기데이터드림](https://data.gg.go.kr/portal/mainPage.do)의 DB를 활용하여 남양주시와 의정부시의 병원 정보를 기재했습니다.</br> 
>사이트 주소: http://3.37.47.173/ 

</br></br>
- 병원 관계자(엘병원) 계정: ID = manager53@naver.com. PW = aoslwj5. </br>
- 관리자 계정: 악용을 방지하기 위해서 mac20010@naver.com 메일로 요청하시면 아이디와 패스워드를 제공하겠습니다.</br>

## 1. 프로젝트 요약 
![프로젝트 요약](https://user-images.githubusercontent.com/40010165/234650806-cac5cefe-b72c-443d-aa90-69520fad1b69.jpg)
</br>

## 2. 프로젝트 구조
### Architecture
![architecture](https://user-images.githubusercontent.com/40010165/216532852-b428aa72-c4da-4ad1-aca3-93b17f6b73c6.png)

### CI/CD
![CICD Flow](https://user-images.githubusercontent.com/40010165/234650714-a5b6e9f7-dc2d-486a-add0-00ba00d2cd40.jpg)
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

## 5. 핵심 트러블슈팅
## 5.1. 사용자 권한
- **사용자 권한은 어떻게 구현할 것이고, 특정 병원 번호만 어떻게 조작이 가능하게 할 것인가?**

- 이걸 어떻게 구현할지 감이 안 온 저는, 우아한형제들 기술이사 김영한 님의 강의를 평소에 보고 있어서 아래와 같은 조언을 얻었습니다. 

> 1. 어떤 방법을 사용하든 권한 체크는 서버에서 추가로 해주어야 한다. </br>
> 2. 병원의 고유 번호(ex.5764)를 가진 사람이 5764번 병원만 정보를 수정하게끔 할 수 있는가 없는가에 대한 판단도 서버에서 모두 계산해서 클라이언트로 내려주는 것이 좋다.
> 
</br>


### (1) 테이블 설계

![설명2](https://user-images.githubusercontent.com/40010165/193619543-bc61ad47-c8bf-4349-a094-c36b60f65d35.png)
  - Authority의 권한 상태는 enum 타입으로 ROLE_USER(사용자), ROLE_MANAGER(병원 관계자), ROLE_ADMIN(관리자) 세 가지로 고정했습니다.
  
  - MemberAuthority 엔티티의 hospitalNo는 병원 번호를 뜻합니다. ROLE_MANAGER(병원 관계자) 권한을 가진 사용자만 병원 번호를 가질 수 있습니다. 
  
  - 멤버는 권한에 따라 여러 개의 권한을 가집니다. 예를 들어 병원 관계자는 USER(사용자), MANAGER(병원 관계자) 2개의 권한을 갖게끔 했습니다.
  
</br>

### (2) JWT Token
![토큰 구조 설명](https://user-images.githubusercontent.com/40010165/203349006-a1681a0d-762e-49cd-9e4b-4427ab4834b3.png)

- JWT Token을 사용했습니다. 프론트에서 서버로 매번 요청할 때마다 doFilter로 Header에서 토큰 정보를 꺼냈습니다. 이를 기반으로 유저 객체를 생성, Authentication 객체를 만들었고 권한을 부여했습니다.

- Spring Security를 활용하여 WebSecurity 설정으로 특정 URL에 들어갈 때 계정에 특정 권한이 있어야지만, 해당 URL에 접근을 허용했습니다.

</br>

 
 ### (3) 병원 번호 확인
- '병원의 고유 번호(ex.5764)를 가진 사람이 어떻게 자신의 병원만 수정하게 할까?' 고심하던 중, token payload의 정보를 가져올 생각을 했습니다.

- 병원 MANAGER 권한을 갖은 계정이 클라이언트에서 수정하려는 병원 번호(pk)를 조작하면 다른 병원의 정보 갱신이 가능해집니다. 이에 아래와 같이 설계했습니다.

![병원 번호 검증](https://user-images.githubusercontent.com/40010165/221642895-6c21f57b-446a-46c2-b0cf-4e651019de0b.png)
 
- 먼저 기존의 doFilter를 통해 Manager 권한을 부여하여, Manager 권한이 필요한 특정 URL에 매번 들어갈 수 있습니다. **(1차 검증)**

- 병원 번호를 확인하는 메소드를 아래와 같이 만들었습니다. 병원 정보를 수정/삭제/추가하려는 경우, 자신이 관리하는 병원 번호인지 확인하기 위해 매번 확인 메소드를 넣었습니다. **(2차 검증)** 

    ~~~java
    public void accessManager(
            ServletRequest servletRequest,
            Long hospitalId
    ) {
        Long hospitalNumberInJwt = getHospitalNumberInJwt(servletRequest);

        if (confirmAdmin(hospitalNumberInJwt)) {
            throw new AccessDeniedException("관리자 계정은 관리자 기능을 이용해주세요.");
        } else if (confirmMatchHospitalNumber(hospitalId, hospitalNumberInJwt)) {
            throw new AccessDeniedException("자신의 병원 번호만 조작이 가능합니다.");
        }
    }
    
    private boolean confirmAdmin(Long hospitalNumberInJwt) {
        return hospitalNumberInJwt.equals(0L);
    }

    private boolean confirmMatchHospitalNumber(Long hospitalId, Long hospitalNumberInJwt) {
        return hospitalNumberInJwt.equals(hospitalId)? false: true;
    }
    

- ServletRequest을 통해 토큰의 병원 번호를 꺼냅니다. 그리고 프론트 엔드에서 수정 요청한 병원 번호와 DB의 병원 번호가 같은지 확인했습니다. 

- MANAGER 권한이 아닌 ADMIN 권한은 병원 번호 default를 0으로 표시했습니다. 그렇기에 0이 나오면 ADMIN 권한으로 판단했습니다. 

</br>

## 6. 기타 트러블슈팅
Back-end 
-------------
<details>
<summary>로그인을 할 때마다 불필요한 쿼리가 발생할 때</summary>
<div markdown="1">

- 로그인 요청이 오면 UserDetailsService 타입의 loadUserByUsername 함수가 실행됩니다.

- 저는 loadUserByUsername 함수에 User 타입으로 반환했었습니다.

- 그런데 User 타입으로는 병원 번호를 불러올 수 없습니다. 이에 Member 정보를 불러오는 쿼리를 또 만들었습니다. 

<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java

### JwtUserDetailsService
  @Service
  @Transactional(readOnly=true)
  @RequiredArgsConstructor
  public class JwtUserDetailsService implements UserDetailsService {
      private final MemberRepository memberRepository;

      @Override
      @Transactional
      public UserDetails loadUserByUsername(String name){
          Optional<Member> memberOptional = memberRepository.findOneEmailByMemberIdName(name);

          Member member = memberOptional.orElseThrow(()->new IllegalStateException("로그인하려는 아이디가 존재하지 않습니다."));

          List<MemberAuthority> memberAuthorities = memberRepository.memberAuthorities(name);

          List<GrantedAuthority> grantedAuthorities = memberAuthorities.stream()
                  .map(a-> new SimpleGrantedAuthority(a.getAuthority().getAuthorizationStatus().toString())).collect(Collectors.toList());

          return new org.springframework.security.core.userdetails.User(member.getMemberIdName()
                  ,member.getPassword(),grantedAuthorities);
      }

### API Controller
    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponse> loginMember(@RequestBody @Validated LoginMemberRequest request){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getMemberIdName(), request.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = memberService.logIn(request.getMemberIdName(), request.getPassword());

            String jwt;

            //멤버 권한이 STAFF 전용 토큰 만들기
            if(member.getMemberStatus() == MemberStatus.STAFF){
                jwt = tokenProvider.createStaffToken(authentication,
                        member.getPhoneNumber(),member.getHospitalNumber());
            }
            //멤버 권한이 관리자나, 일반 유저라면
            else  {
                jwt = tokenProvider.createToken(authentication, member.getPhoneNumber());
            }

            //토큰 null 체크.
            if(jwt == null){
                throw new IllegalStateException("토큰 값이 null 입니다.");
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new ResponseEntity<>(new LoginMemberResponse(member.getId(), member.getNickName(), member.getMemberStatus(), jwt), httpHeaders, HttpStatus.OK);
    }
~~~

</div>
</details>

- 위 코드의 문제점은 로그인하는데 loadUserByUsername 함수 안에 있는 쿼리로도 충분한데, API Controller의 ` Member member = memberService.logIn(request.getMemberIdName(), request.getPassword());`로 불필요한 쿼리를 호출합니다. 이러면 로그인을 할 때마다 성능 저하가 발생할 수 있습니다.

- 디버깅을 통해 authentication의 principal에 username, authorities, password만 있는 것을 인지했습니다. (잡다한 변수는 생략했다.)

- 구글 검색으로 User의 데이터 수를 늘릴 방법을 찾던 중에 커스트마이징을 통해 수를 늘릴 수 있는 것을 알아냈습니다.  

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java


@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name){
        Optional<Member> memberOptional = memberRepository.findOneEmailByMemberIdName(name);

        Member member = memberOptional.orElseThrow(()->new IllegalStateException("로그인하려는 아이디가 존재하지 않습니다."));

        List<MemberAuthority> memberAuthorities = memberRepository.memberAuthorities(name);

        List<GrantedAuthority> grantedAuthorities = memberAuthorities.stream()
                .map(a-> new SimpleGrantedAuthority(a.getAuthority().getAuthorizationStatus().toString())).collect(Collectors.toList());

        return new CustomUserDetail(member.getMemberIdName()
                ,member.getPassword(),grantedAuthorities,
                member.getPhoneNumber(),member.getHospitalNumber(),
                member.getId(),member.getNickName(),member.getMemberStatus());
    }

    public class CustomUserDetail extends User {

        String phoneNumber;
        Long hospitalNumber;
        Long memberId;
        String nickName;
        MemberStatus memberStatus;

        public CustomUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities,
                                String phoneNumber, Long hospitalNumber,
                                Long memberId, String nickName, MemberStatus memberStatus) {
            super(username, password, authorities);
            this.phoneNumber = phoneNumber;
            this.hospitalNumber = hospitalNumber;
            this.memberId = memberId;
            this.nickName = nickName;
            this.memberStatus = memberStatus;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public Long getHospitalNumber() {
            return hospitalNumber;
        }

        public Long getMemberId() {
            return memberId;
        }

        public String getNickName() {
            return nickName;
        }

        public MemberStatus getMemberStatus() {
            return memberStatus;
        }
    }

}


### API Controller
    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponse> loginMember(@RequestBody @Validated LoginMemberRequest request){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getMemberIdName(), request.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //jwt Token
            String jwt;
            //커스텀 사용자 객체 가져오기
             JwtUserDetailsService.CustomUserDetail user =
                     (JwtUserDetailsService.CustomUserDetail) authentication.getPrincipal();


             //멤버 권한이 일반 유저라면
             if(!authentication.getAuthorities().stream()
                     .anyMatch(a->a.getAuthority().equals("ROLE_MANAGER")) &&

                     authentication.getAuthorities().stream()
                     .anyMatch(a->a.getAuthority().equals("ROLE_USER"))
             )
             {
                jwt = tokenProvider.createToken(authentication, user.getPhoneNumber());
             }
             //멤버 권한이 STAFF 전용 토큰 만들기
            else if(!authentication.getAuthorities().stream()
                    .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")) &&

                     authentication.getAuthorities().stream()
                    .anyMatch(a->a.getAuthority().equals("ROLE_MANAGER"))
             )
            {
                jwt = tokenProvider.createStaffToken(authentication,
                        user.getPhoneNumber(), user.getHospitalNumber());
            }
            //멤버 권한이 관리자라면
            else if(authentication.getAuthorities().stream()
                     .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))) {
                jwt = tokenProvider.createToken(authentication, user.getPhoneNumber());
            }
            else{
                throw new IllegalStateException("권한이 존재하지 않습니다.");
             }

            //토큰 null 체크.
            if(jwt == null){
                throw new IllegalStateException("토큰 값이 null 입니다.");
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new ResponseEntity<>(new LoginMemberResponse(user.getMemberId(), user.getNickName(), user.getMemberStatus(), jwt), httpHeaders, HttpStatus.OK);
    }
~~~

</div>
</details>

</div>
</details>

<details>
<summary> Q&A에서 답변의 NullPointerException 문제</summary>
<div markdown="1">

- Q&A를 조회할 때 답변이 없는 경우(null)도 나타내기 위해서 leftJoin을 사용. :clipboard: [코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/main/java/site/hospital/repository/question/QuestionRepositoryImpl.java#L31)

- Answer 객체의 answerId나, answerContent를 불러오면 자꾸 NullPointerException 문제가 발생.
  
<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~java

    //병원 관계자 Questions 검색
    @GetMapping("/staff/question/search")
    public Page staffSearchQuestions(ServletRequest servletRequest,
                                               @RequestParam(value="nickName",required = false) String nickName,
                                               @RequestParam(value="memberIdName",required = false) String memberIdName,
                                               Pageable pageable){
        StaffQuestionSearchCondition condition = StaffQuestionSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).build();

        Page<Question> questions = questionService.staffSearchHospitalQuestion(servletRequest,condition,pageable);

        List<SearchHospitalQuestionResponse> result = questions.stream()
                .map(q->new SearchHospitalQuestionResponse(q))
                .collect(Collectors.toList());

        Long total = questions.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }
  

    @Data
    private static class SearchHospitalQuestionResponse{
        private Long questionId;
        private String memberIdName;
        private String nickName;
        private String content;
        private Long answerId;
        private String answerContent;

        public SearchHospitalQuestionResponse(Question question) {
            this.memberIdName = question.getMember().getMemberIdName();
            this.questionId = question.getId();
            this.nickName = question.getMember().getNickName();
            this.content = question.getContent();
            this.answerId = question.getAnswer().getId();
            this.answerContent = question.getAnswer().getAnswerContent();
        }
    }  
~~~

</div>
</details>
    
- 답변이 없는 QnA를 처리할 때 발생하는 문제임을 인식하여, Answer의 객체 탐색 단계 때 null이 반환되지 않을지 의심했습니다.
  
- DTO 생성 시 답변이 없으면, 이에 대한 예외 로직을 아래와 같이 추가.
  
<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~java
    @Data
    private static class SearchHospitalQuestionResponse{
        private Long questionId;
        private String memberIdName;
        private String nickName;
        private String content;
        private Long answerId;
        private String answerContent;

        public SearchHospitalQuestionResponse(Question question) {
            this.memberIdName = question.getMember().getMemberIdName();
            this.questionId = question.getId();
            this.nickName = question.getMember().getNickName();
            this.content = question.getContent();

            if(question.getAnswer() !=null) {
                this.answerId = question.getAnswer().getId();
                this.answerContent = question.getAnswer().getAnswerContent();
            }
        }
    }  
~~~

</div>
</details>
  
</div>
</details>
  
<details>
<summary>multipart 형태의 파일을 올릴 때 null로 받아지는 경우</summary>
<div markdown="1">

- 검색을 통해 `@RequestBody`는 content-Type이 multipart/form-data로 전달될 때, Exception을 발생시켜 문제가 발생함을 인지.

- multipart/form-data일 경우에는 `@RequestPart` 혹은 `@RequestParam`을 사용. 무수한 데이터 종류를 전송할 경우 `@modelAttribute`가 적합한 걸 알아냈다.
  
- 저는 전송 데이터 종류가 적기에 `@RequestParam`을 사용했습니다. 하나의 이미지는 파라미터로 MultipartFile을 사용했고, 다수의 이미지는 List<MultipartFile>를 사용했습니다.
  
- 프론트 엔드에서는 key와 value로 저장할 수 있는 FormData를 사용. vue의 form태그에는 entype 속성값을 multipart/form-data로 설정해서 이미지 파일을 전송했습니다.
</div>
</details>

Front-end 
-------------
<details>
<summary> vue에서 props 데이터가 계속 늦게 도착해 카카오 맵이 정상 출력이 안 된 문제 </summary>
<div markdown="1">

- 초기에는 mounted hook에서 지도를 출력하게 했습니다. 

- 하지만 props로 DB의 좌표값을 받아와도 지도가 정상 출력되지 않았습니다. 

<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~javascript

mounted(){
      if (window.kakao && window.kakao.maps) {
        this.initMap()
      } else {
          const script = document.createElement('script')
          script.onload = () => kakao.maps.load(this.initMap);
          script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=bcdb595a4b1c6bc005200d3b0d9271fb'
          document.head.appendChild(script)
      }
}
~~~

</div>
</details>

- console.log를 통해서 props의 지도 좌표 DB값이 늦게 도착해서, 카카오 맵이 초기값을 먼저 출력해 지도가 정상적으로 안 나오는 걸 인식.

- [검색을 통해 vue의 life cycle 개념을 확인.](https://any-ting.tistory.com/42) props로 좌표값을 받아오기 전 mounted된 것이 먼저 실행된 것을 인지했습니다. 

- props로 데이터를 정상적으로 받는, 상태 변화에 반응하기 위해서 computed 혹은 watch를 사용해야 함을 인식. 

- 저는 watch로 아래의 코드 처럼 props값이 온 걸 확인하면 지도를 출력했습니다. 

<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">

~~~javascript

  watch:{
    detailed: function(){
      if (window.kakao && window.kakao.maps) {
        this.initMap()
      } else {
          const script = document.createElement('script')
          script.onload = () => kakao.maps.load(this.initMap);
          script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=bcdb595a4b1c6bc005200d3b0d9271fb'
          document.head.appendChild(script)
      }
    }
  },
~~~ 

:clipboard: [자세한 코드 확인](https://github.com/kimjungwon2/hospital/blob/master/src/frontend/src/components/hospital/ViewMapForm.vue#L22)
</div>
</details>

</div>
</details>

<details>
<summary>vue에서 배열에 인덱스값을 넣을 때 반응을 못할 경우</summary>
<div markdown="1">

- 아래와 같이 코드를 작성했을 때 배열의 값들이 변하지 않았습니다. 
  
<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

~~~javascript
        async loadReviewAllLike(length){
            const memberId= this.$store.getters.getMemberId;

            for(let i=0; i<length; i++){
                const isReviewLike = await isLikeReview(memberId, this.reviews[i].reviewId);
                this.reviewLike[i] = isReviewLike.data.isReviewLike; 
            }
        },
~~~

</div>
</details>
   
- [vue 공식 문서](https://v3.ko.vuejs.org/guide/change-detection.html#%E1%84%87%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%AF)를 통해 배열 index로 항목을 직접 설정하거나, 배열 길이를 수정하는 경우. 배열의 변화를 감지할 수 없음을 인지. 

- this.$set 사용으로 반응성 시스템에서 상태 변경을 발생시키게 했다. 
 
<details>
<summary><b>개선된 코드</b></summary>
<div markdown="1">
  
~~~javascript
        async loadReviewAllLike(length){
            const memberId= this.$store.getters.getMemberId;

            for(let i=0; i<length; i++){
                const isReviewLike = await isLikeReview(memberId, this.reviews[i].reviewId);
                this.$set(this.reviewLike, i, isReviewLike.data.isReviewLike);
            }
        },
~~~
                                   
</div>
</details>  

</div>
</details>
  
</br>

## 7. 고려한 점
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
이렇게 modifyEstimation 함수명으로 **정보를 수정한다는 걸 한눈에 알 수 있습니다**. 

</div>
</details>

<details>
<summary>엔티티나 임베디드 타입은 기본 생성자를 protected로 설정했습니다. 저는 @NoArgsConstructor(access = AccessLevel.PROTECTED) 롬복으로 대체했습니다.</summary>
<div markdown="1">

- Setter로 타인이 무분별하게 값을 변경하는 걸 방지하기 위해, protected 생성자로 아무 데나 생성되는 걸 제약한다.
- **private를 사용 못하는 이유**: JPA 표준 스펙에 디폴트 생성자가 있어야 합니다. JPA가 프록시 기술을 쓸 때,  jpa hibernate가 객체를 강제로 만들어야 하는데 private로 만들면 이것이 다 막힙니다.
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

- merge 사용 시, 값이 없으면 null로 업데이트되기에 변경 감지를 사용했습니다.
</div>
</details>

<details>
<summary>Auditing으로 등록일, 수정일을 다 넣었습니다. </summary> 
<div markdown="1">

- 데이터를 언제 바꿨냐, 언제 문제가 생겼냐는 게 중요해서 넣으면 운영할 때 편합니다.
- 등록, 수정 두 가지는 모든 테이블에 다 적용했습니다.
</div>
</details>

<details>
<summary>API 계층에서 Entity를 그대로 파라미터에 넣거나, Entity 값으로 반환하지 않았습니다. 대신 API 스펙을 위한 별도의 DTO를 생성했습니다.</summary>
<div markdown="1">

- Entity를 웹에 노출하면 api 스펙이 변해버리거나 패스워드가 그대로 노출되기에, DTO로 변환해줘야 합니다.
</div>
</details>

<details>
<summary>페이징 시 countQuery를 따로 분리했습니다.</summary>
<div markdown="1">

- 분리함으로써 성능을 최적화했습니다.
</div>
</details>

</br>

## 8. 회고&느낀점
>프로젝트 개발 회고 글: https://kjw1313.tistory.com/49
