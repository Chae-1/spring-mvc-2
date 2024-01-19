package hello.thymeleaf.basic.login.web;

import hello.thymeleaf.basic.form.domain.member.Member;
import hello.thymeleaf.basic.form.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberRepository repository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute Member member,
                          Model model) {
        model.addAttribute("member", member);
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute Member member,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "members/addMemberForm";
        }

        repository.save(member);
        log.info("{}", member);
        return "redirect:/";
    }

}
