package com.example.demo.controller;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signup")
    public String getsignup() {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String postsignup(Member member, RedirectAttributes rttr) {
        if (member.getAge() < 1 || member.getEmail() == null ||
                member.getEmail().trim().isEmpty() || member.getName() == null ||
                member.getName().trim().isEmpty() || member.getPassword() == null ||
                member.getPassword().trim().isEmpty()) {
            rttr.addFlashAttribute("msg", "값을 모두 입력해주세요");
            return "redirect:/member/signup";
        }

        member.setName(member.getName().trim());
        member.setEmail(member.getEmail().trim());
        member.setPassword(member.getPassword().trim());

        boolean c = memberService.insertMember(member);
        if (c) {
            rttr.addFlashAttribute("msg", "회원가입 완료!");
            return "redirect:/";
        } else {
            rttr.addFlashAttribute("msg", "회원가입 실패!");
            return "redirect:/member/signup";
        }
    }

    @GetMapping("/login")
    public String getlogin() {
        return "member/login";
    }

    @PostMapping("/login")
    public String Postlogin(@RequestParam("id") String id, @RequestParam("password") String password,
                            RedirectAttributes rttr, HttpSession session) {
        if (id == null || id.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            rttr.addFlashAttribute("msg", "값을 모두 입력해주세요");
            return "redirect:/member/login";
        }

        Member member = memberService.loginMember(id, password);

        if (member != null) {
            session.setAttribute("user_id", member.getId());
            session.setAttribute("user_name", member.getName());
            rttr.addFlashAttribute("msg", "로그인 완료!");
            return "redirect:/";
        } else {
            rttr.addFlashAttribute("msg", "아이디나 비밀번호를 확인해주세요.");
            return "redirect:/member/login";
        }
    }

    @GetMapping("/logout")
    public String getlogout(RedirectAttributes rttr, HttpSession session) {
        if (session != null) {
            session.invalidate();
            rttr.addFlashAttribute("msg", "로그아웃 완료!");
            return "redirect:/";
        } else {
            rttr.addFlashAttribute("msg", "잘못된 접근입니다.");
            return "redirect:/";
        }
    }

    @GetMapping("/detail")
    public String getDetail(HttpSession session, Model model, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        Member member = memberService.selectMember(userId);
        model.addAttribute("user", member);
        return "member/detail";
    }

    @PostMapping("/detail")
    public String PostDetail(HttpSession session, Member member, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        if (member.getEmail() == null || member.getEmail().trim().isEmpty() ||
                member.getName() == null || member.getName().trim().isEmpty() ||
                member.getPassword() == null || member.getPassword().trim().isEmpty()) {
            rttr.addFlashAttribute("msg", "값을 모두 입력해주세요");
            return "redirect:/member/detail";
        }

        member.setId(userId);

        Member mem = memberService.updateMember(member);

        if (mem != null) {
            rttr.addFlashAttribute("msg", "회원정보 수정 완료! 다시 로그인 해주세요!");
            session.removeAttribute("user_id");
            session.removeAttribute("user_name");
            return "redirect:/member/login";
        } else {
            rttr.addFlashAttribute("msg", "회원정보 수정 실패.");
            return "redirect:/member/detail";
        }
    }
}