## 서블릿 필터
모든 로직에서 공통적으로 처리해야하는 로직을, 공통 관심사라고 한다. 애플리케이션의 등록, 삭제, 조회 등등, 
여러곳에서 인증에 대한 관심을 가지고 있다.

웹과 관련된 공통 관심사를 처리하기 위해 서블릿은 필터기능을 지원한다.

서블릿 필터를 적용하면, 서블릿이 호출되기 이전에 필터가 호출된다. 여기서 비정상적인 처리라면 다른 요청으로 리다이렉트를 시킬 수 있다.

필터의 흐름은 다음과 같다.

```text
HTTP 요청 -> WAS -> 서블릿 필터 -> 서블릿(DispatcherServlet) -> 컨트롤러
```

특정 URL 패턴에 필터가 적용되도록 적용할 수 있다.

필터는 체인 형태로 구성돼, 자유롭게 추가할 수 있다.

```java
public interface Filter {
    public default void init(FilterConfig filterConfig) throws ServletException
    {}
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException;
    public default void destroy() {}
}
```

- init(): 서블릿 필터가 등록될 때, 호출되는 메서드, 서블릿 컨테이너가 생성될 때, 호출
- doFilter(): 서블릿 필터의 대상이 되는 URL이 호출 됐을 때, 호출되는 메서드.
- destroy(): 서블릿 컨테이너가 종료될 때, 호출

`doFilter` 메서드를 만들 때, FilterChain의 doFilter를 호출해야 다음 필터를 호출하거나, 서블릿을 호출한다.

## 스프링 인터셉터
스프링 MVC가 제공하는 기술, 서블릿 필터와 같이 웹의 공통 관심사를 처리한다.

하지만, 호출되는 순서가 다르다.

```text
HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
```
- 스프링 인터셉터는 컨트롤러를 호출하기 이전에 작동한다.
- 스프링이 제공하는 기능, 디스패처 서블릿이후에 호출된다.
- 필터와 다르게 매우 정밀하게 URL 패턴을 설정할 수 있다.

스프링 인터셉터를 사용하려면, `HandlerInterceptor` 인터페이스를 구현하면 된다.
```java
public interface HandlerInterceptor {
default boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                          Object handler) throws Exception {}
default void postHandle(HttpServletRequest request, HttpServletResponse response,
                        Object handler, @Nullable ModelAndView modelAndView) 
throws Exception {}
default void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                             Object handler, @Nullable Exception ex) throws
Exception {}
}
```
- `preHandle`: 핸들러 어댑터를 호출하기 이전에 호출된다.
  - return 값이 true, 다음 인터셉터나, 핸들러 어댑터를 호출 한다.
- `postHandle`: 정상적으로 핸들러 어댑터를 호출한 이후에 호출된다.
- `afterCompletion`: 예외 상황에서도 반드시 호출된다.

예외 상황에서는 preHadnle, afterCompletion만 호출 된다.


## ArgumentResolver
- 핸들러 어댑터에서 컨트롤러를 호출하기 이전에 `ArgumentResolver`를 활용해서 파라미터를 조회한다.
- 이를 이용해, 컨트롤러에서 받을 수 있는 파라미터를 확장할 수 있다.


