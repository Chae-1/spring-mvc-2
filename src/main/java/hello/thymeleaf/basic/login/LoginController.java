package hello.thymeleaf.basic.login;

import hello.thymeleaf.basic.form.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm,
                            Model model) {
        model.addAttribute("loginForm", loginForm);
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginV1(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        Cookie cookie = new Cookie("memberId", String.valueOf(member.getId()));
        response.addCookie(cookie);

        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }
        return "redirect:/";
    }
}
