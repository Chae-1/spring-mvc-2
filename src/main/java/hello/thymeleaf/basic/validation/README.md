검증
==
입력 값에 대한 검증은, 정상 로직을 처리하는 것보다 중요할 수도 있다.

다음과 같은 필드에 대한 검증 로직을 추가해보자.
- 상품명은 필수, 공백이 들어와서는 안된다.
- 가격은 1000원 이상, 100만원 이하
- 수량은 최대 9999개를 넘어서면 안된다.

이를 적용시키기 위해, 기존 컨트롤러에서 다음과 같이 처리한다.
- 입력 값이 정상인 경우, 상품 상세를 보여준다.
- 그렇지 않은 경우, 입력 폼을 다시 보여주고 어떤 에러 메시지인지 확인 시켜준다.

수동으로 검증을 했을 때, 다음과 같은 문제가 남아있다.
- 타입 에러를 처리하지 못한다.
  - 컨트롤러에 넘어오기 이전에 400 에러가 발생한다.
  - 고객이 입력한 값도 별도로 관리가 되어야 한다.


## BindingResult
스프링은 편리한 검증을 지원하기 위해 `BindingResult`라는 클래스를 지원한다. 다음과 같은 규칙을 지켜야한다.
- `BindingResult`는 항상, `@ModelAttribute` 매개변수 바로 뒤에 위치해야한다.

다음과 같은 특징을 지닌다.
- 필드 에러를 처리하기 위해, `BindingResult`에 `FieldError` 객체를 집어넣을 수 있다.
- 객체의 에러를 처리하기 위해, `BindingResult`에 `ObjectError` 객체를 집어넣을 수 있다.
- 타임리프에서 필드 에러가 존재하는 경우 이를 출력해주는, `th:errors`, `th:errorclass`가 존재하고
- `BindingResult`에 접근할 수 있는 편의 객체인 `#fields`를 제공한다.
- `Errors` 라는 인터페이스를 상속받고 있다.

`BindingResult`가 존재하면, 타입 에러가 발생해도 컨트롤러가 호출된다. 

## FieldError, ObjectError
필드 에러가 발생했을 때, 기존 메시지가 남지 않는 문제가 존재했다. `FieldError`, `ObjectError`의 다른 생성자를 이용하면 이를 해결할 수 있다.
```java
FieldError(String objectName, String field, String defaultMessage);
FieldError(String objectName, String field, @Nullable Object
        rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable 
Object[] arguments, @Nullable String defaultMessage);
```
`FieldError` 클래스는 두개의 생성자가 존재한다.
- `objectName`: 오류가 발생한 객체의 이름
- `field`: 오류 필드
- `defaultMessage`: 기본 오류 메시지
- `rejectValue`: 오류를 발생시킨 값(이전 사용자의 입력)
- `bindingFailure`: 바인딩 실패인지, 검증 실패인지 구분
- `codes`: 오류가 발생했을 때, 보여줄 메시지 코드(MessageSource에서 읽어 들인다.)
- `arguments`: 메시지 코드에 전달 할 매개 변수

`codes`, `arguments`를 사용해 메시지 처리를 할 수 있다.

일일이 `FieldError`, `ObjectError`를 등록하는 번거러움을 없애기 위해, `BindingResult`는 `reject`, `rejectValue` 메서드를 제공한다.

이는, `BindingResult`가 이미 검증할 객체를 알고 있기 때문에 가능한 것이다.
- `rejectValue`: 필드값을 검증
- `reject`: 객체를 검증

두 메서드 내부에서 `MessageCodesResolver`를 사용해 예상 코드를 생성하여 `FieldError`, `ObjectError`를 등록한다.
- 스프링 부트는 `DefaultMessageCodesResolver`를 등록하고 있다.
- 코드 생성 규칙은 다음과 같다.
- field: age, 오류 코드: typeMismatch, field type: String, object : student
```text
1: code + "." + object + "." + fieldType
2: code + "." + fieldType
3: code + "." + type
4: code
```

## Validator
컨트롤러에서 검증 로직의 차지하는 부분으로 인해 별도의 클래스로 역할을 분리하는 것이 좋다. 
스프링에서 검증을 체계적으로 제공하기 위해 `Validator` 인터페이스를 제공한다.

추가적으로 이 인터페이스를 구현 클래스를 `WebDataBinder`에 등록을 하면 `@Validated` 어노테이션을 적용할 수 있다.

**`@Validated`**
해당 애노테이션은 등록된 검증기를 찾아서 실행하라는 애노테이션이다.
`supports()`를 호출한 후, true라면 `validate`를 호출한다.


## Bean Validation
검증 기능을 코드로 작성하는 것이 아닌, 애노테이션을 활용해 검증을 편리하게 적용할 수 있다.

Bean Validation 이라는 기술을 이용하면 이를 쉽게 적용할 수 있다.

```java
@Data
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

}
```

- 의존성을 추가하면, 스프링 부트가 자동으로 Bean Validator를 글로벌로 등록한다.
- `@Valid` or `@Validated`가 적용된 클래스를 검증한다.
- 검증 오류가 발생하면, `FieldError`, `ObjectError`를 `BindingResult`에 담는다.


**검증 순서**
1. `@ModelAttribute` 각각의 필드에 타입 변환 시도
   1. 실패하면, 오류코드 `typeMismatch`로 `FieldError`를 등록한다.
2. Validator 적용

검증 시, 바인딩에 성공한 필드만 검증기를 통해 검증을 한다.


**에러 메세지 생성 규칙**

Bean Validation에 등록된 애노테이션의 이름이 오류 코드가 된다. 

예를 들어 객체명이 person, fieldName이 name이고 타입이 String, `@NotBlank`가 적용 됐다고 한다면,

1. NotBlank.person.name
2. NotBlank.name
3. NotBlank.java.lang.String
4. NotBlank

이렇게 4가지를 메세지 코드로 등록한 `FieldError`를 생성해낸다. 이를 고려해 메시지를 구성하면 된다.


**한계**
- 등록, 수정시 요구사항이 달라 필드의 검증 내용이 다를 수 있다.

해당 문제는 groups, Form 데이터 객체를 따로 생성해서 해결할 수 있다.

**groups**
- 해당 한계점을 고려해 검증할 그룹을 지정할 수 있는 기능을 제공한다.
```java

@Data
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank(groups = {AddForm.class, EditForm.class})
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000, groups = {AddForm.class, EditForm.class})
    private Integer price;

    @NotNull(groups = {AddForm.class, EditForm.class})
    @Max(value = 9999, groups = AddForm.class)
    private Integer quantity;

}


```

- 하지만, groups 보다 등록, 수정 Form 데이터 객체를 만들어 해결하는 것이 깔끔하다.


