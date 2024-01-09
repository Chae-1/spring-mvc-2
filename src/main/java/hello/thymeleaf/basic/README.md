타임 리프
==
- 네츄럴 템플릿
- 서버 사이드 HTML 렌더링
- 스프링 통합 지원

**서버 사이드 렌더링(SSR)**

백엔드 서버에서 HTML을 렌더랑하는 용도로 사용한다.


**네츄럴 템플릿**

순수 HTML을 유지하면서 뷰 템플릿으로도 사용할 수 있는 템플릿을 네츄럴 템플릿이라 한다.


## 타임 리프 - 기본 기능 

#### 텍스트 - text, utext
- HTML 태그의 속성을 렌더링할 때, 동적으로 바꾸는 기능
- 콘텐츠를 교체하기 위해서 `th:text`를 사용한다.
- `th:text`는 태그를 사용해야하지만, 직접 내용을 삽입하기 위해선 `[[${}]]` 문법을 사용한다.

**Escape**
- 태그에서 `<`, `>`를 사용하기 때문에, 내부에서 사용하기 때문에 템플릿에서 생성하면 Escape처리가 된다.
- `th:text`, `[[${}]]`에서는 Escape를 사용한다.
- 사용하지 않기 위해선 `th:utext`를 사용하고 `[[]]` 대신, `[()]`를 사용한다.


#### SpringEL - 변수 표현식
- 변수 표현식 - `${...}`
- SpringEL 표현식
  - Object: ${user.username}, ${user['username']}, ${user.getUsername()}
  - List: ${user[0]}
  - Map: ${userMap['key']}
 
#### th:with
지역 변수를 선언할 수 있다.

#### 기본 객체
- `${#locale}` - locale
스프링이 제공하는 표현식을 사용할 수도 있다.

#### 편의 객체
- Request parameter 조회 시, `${param}`을 통해 조회할 수 있다.
- Session 객체에 담긴 속성을 조회할 때, 바로 조회할 수 있다.
- Spring bean을 직접 호출할 수 있다.


#### 유틸리티 객체