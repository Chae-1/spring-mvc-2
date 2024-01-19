package hello.thymeleaf.basic.filterAndInterceptor;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

@Slf4j
public class LoginFilter implements Filter {
    private static final String[] whiteList = {"/login", "/members/add", "/logout", "/css/*", "/"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //1. 인증 체크
        log.info("로그인 필터 작동");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        // 세션이 생성된 상태라면, 로그인이 된 상태
        // 생성된 상태가 아니라면 홈으로 리다이렉트 후, 필터 호출 중단
        String requestURI = httpRequest.getRequestURI();
        if (isNotWhiteList(requestURI)) {
            if (session == null || session.getAttribute("member") == null) {
                log.info("미인증 사용자");
                httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                return;
            }
        }
        // 정상 호출
        chain.doFilter(request, response);


    }

    public boolean isNotWhiteList(String requestURI) {
        // 패턴과 매칭이 되면 false
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
