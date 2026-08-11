package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


    @GetMapping("/detail")
    public String getPostDetail(@RequestParam("id") int id, Model model){
        Post post = postService.selectPost(id);
        model.addAttribute("post", post);
        return "post/detail";
    }

    @GetMapping("/update")
    public String getUpdate(@RequestParam("post_id") int postId, HttpSession session, Model model, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요한 서비스입니다.");
            return "redirect:/member/login";
        }

        Post post = postService.selectPost(postId);
        if (post.getMember_id() != userId) {
            rttr.addFlashAttribute("msg", "비정상 접근입니다.");
            return "redirect:/";
        }

        model.addAttribute("post", post);
        return "post/update";
    }

}

