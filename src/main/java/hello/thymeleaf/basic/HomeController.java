package hello.thymeleaf.basic;

import hello.thymeleaf.basic.form.domain.member.Member;
import hello.thymeleaf.basic.form.domain.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    @GetMapping("/")
    public String home(@CookieValue(required = false, name = "memberId") Long memberId,
                       Model model) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            return "home";
        }

        return "loginHome";
    }
}
