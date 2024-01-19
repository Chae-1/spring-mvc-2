package hello.thymeleaf.basic.login;

import hello.thymeleaf.basic.form.domain.member.Member;
import hello.thymeleaf.basic.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm,
                            Model model) {
        model.addAttribute("loginForm", loginForm);
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String loginV1(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }
        Cookie cookie = new Cookie("memberId", String.valueOf(member.getId()));
        response.addCookie(cookie);


        return "redirect:/";
    }

//    @PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }
        sessionManager.createSession(response, member);
        return "redirect:/";
    }

//    @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }
        // 신규 세션을 생성
        HttpSession session = request.getSession(true);
        // 세션에 회원 정보를 저장 ->
        session.setAttribute("member", member);
        return "redirect:/";
    }

//    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }
        // 신규 세션을 생성
        HttpSession session = request.getSession(true);
        // 세션에 회원 정보를 저장 ->
        session.setAttribute("member", member);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV5(@Validated @ModelAttribute LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpServletRequest request,
                          @RequestParam(defaultValue = "/") String redirectURL) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }
        // 신규 세션을 생성
        HttpSession session = request.getSession(true);
        // 세션에 회원 정보를 저장 ->
        session.setAttribute("member", member);
        return "redirect:" + redirectURL;
    }





    //    @PostMapping("/logout")
    public String logoutV1(@CookieValue(name = "memberId") Long memberId, HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", String.valueOf(memberId));
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

//    @PostMapping("/logout")
    public String logoutV2(@CookieValue(name = SessionManager.SESSION_ID) String sessionId) {
        sessionManager.expire(sessionId);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/";
    }

}
