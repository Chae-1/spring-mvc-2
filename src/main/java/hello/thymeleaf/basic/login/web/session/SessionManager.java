package hello.thymeleaf.basic.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {
    public static final String SESSION_ID = "sessionId";
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void createSession(HttpServletResponse response, Object value) {
        // 1. 세션 생성, 세션 키 값으로 중요 정보 저장
        String sessionId = UUID.randomUUID().toString();
        store.put(sessionId, value);

        // 2. 세션 키 값을 쿠키로 전달
        Cookie sessionCookie = new Cookie(SESSION_ID, sessionId);
        response.addCookie(sessionCookie);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie cookie = findCookie(request);
        if (cookie == null) {
            return null;
        }
        return store.get(cookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(SESSION_ID))
                .findFirst()
                .orElse(null);
    }

    public void expire(String sessionId) {
        store.remove(sessionId);
    }
}
