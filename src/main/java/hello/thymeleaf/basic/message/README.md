메세지, 국제화
==
스프링은 여러 화면에서 공통으로 존재하는 단어들을 변경하기 위해 메시지 기능을 제공한다.
- `messages.properties`라는 메시지 관리용 파일을 생성한 후, 이를 불러들여 사용하는 기능을 제공한다.

이를 확장하여 각 나라의 언어별로 메시지를 관리하여 서비스를 국제화할 수 있다.

이 모든 것은 스프링 부트에서 자동으로 `MessageSource`를 등록했기 때문에 가능한 것이다.

```java
public interface MessageSource {

    @Nullable
    String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale);

    String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;
    // ...
}
```
- `MessageSource` 인터페이스는 메시지를 읽어들이는 기능을 제공한다.
- `Locale` 정보를 입력받아서 메시지를 출력해주는 기능을 수행한다.
- 존재하지 않는 메시지를 읽으면 예외가 발생한다. 이를 방지하고자 defaultMessage를 지정할 수 있도록 오버로딩 돼 있다.
- `Locale`을 null 값으로 지정하면, 시스템 로케일을 기반으로 메시지를 읽어들인다. 지원하지 않는 Locale일 경우, 디폴트 메시지를 읽어들인다.
  - default: 'messages.properties'
- 또한, `Object[] args` 매개 변수를 통해 동적으로 메시지를 교체할 수 있다.
- 타임리프에서는 메시지를 읽어들일 수 있도록 메시지 표현식 `#{}`을 지원한다.


## 국제화 적용
스프링에서는 국제화를 적용하기 위해 `Accept-Language` 헤더의 값을 보고 로케일을 지정한다.

`Locale` 선택 방식을 지원하기 위해, `LocaleResolver`라는 인터페이스를 지원한다.