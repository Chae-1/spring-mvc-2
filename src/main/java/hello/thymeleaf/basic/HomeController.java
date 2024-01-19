package hello.thymeleaf.basic;

import hello.thymeleaf.basic.filterAndInterceptor.Login;
import hello.thymeleaf.basic.form.domain.member.Member;
import hello.thymeleaf.basic.form.domain.member.MemberRepository;
import hello.thymeleaf.basic.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;
//    @GetMapping("/")
    public String home(@CookieValue(required = false, name = "memberId") Long memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            return "home";
        }
        log.info("member = {}", member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeV2(HttpServletRequest request, Model model) {
        Member member = (Member) sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }
        log.info("member = {}", member);
        model.addAttribute("member", member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeV3(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "home";
        }
        log.info("member = {}", member);
        model.addAttribute("member", member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeV4(@SessionAttribute(name = "member", required = false) Member member, Model model) {
      if (member == null) {
            return "home";
        }
        log.info("member = {}", member);
        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeV5(@Login Member member, Model model) {
      if (member == null) {
            return "home";
        }
        log.info("member = {}", member);
        model.addAttribute("member", member);
        return "loginHome";
    }

}
