package com.example.demo.controller;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/detail")
    public String getPostDetail(@RequestParam("id") int id, Model model) {
        Post post = postService.selectPost(id);
        List<Comment> comments = commentService.showComment(id);
        model.addAttribute("comments", comments);
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

    @PostMapping("/update")
    public String postUpdate(Post post, HttpSession session, RedirectAttributes rttr) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || !Objects.equals(post.getMember_id(), userId)) {
            rttr.addFlashAttribute("msg", "비정상 접근입니다.");
            return "redirect:/";
        }
        boolean isSuccess = postService.updatePost(post);
        if (isSuccess) {
            rttr.addFlashAttribute("msg", "게시글 수정에 성공했습니다.");
            return "redirect:/post/detail?id=" + post.getId();
        } else {
            rttr.addFlashAttribute("msg", "게시글 수정에 실패했습니다.");
            return "redirect:/";
        }
    }

    @GetMapping("/insert")
    public String getInsert() {
        return "post/insert";
    }

    @PostMapping("/insert")
    public String postInsert(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            RedirectAttributes rttr,
            HttpSession session) {

        if (title == null || title.trim().isEmpty() ||
                content == null || content.trim().isEmpty()) {
            rttr.addFlashAttribute("msg", "잘못된 입력입니다");
            return "redirect:/post/insert";
        }
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            rttr.addFlashAttribute("msg", "로그인이 필요한 서비스입니다.");
            return "redirect:/member/login";
        }
        boolean isInserted = postService.insertPost(title, content, userId);
        if (isInserted) {
            rttr.addFlashAttribute("msg", "게시글 정상 등록완료!");
        } else {
            rttr.addFlashAttribute("msg", "게시글 등록 실패!");
        }
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String getDelete(HttpSession session, @RequestParam("id") int post_id, RedirectAttributes rttr) {
        Integer user_id = (Integer) session.getAttribute("userId");
        String c = postService.deletePost(post_id, user_id);

        if (c.equals("아이디 불일치")) {
            rttr.addFlashAttribute("msg", "본인만 삭제할수 있습니다.");
            return "redirect:/";
        } else if (c.equals("실패")) {
            rttr.addFlashAttribute("msg", "게시물 삭제에 실패했습니다.");
            return "redirect:/post/detail?id=" + post_id;
        }
        rttr.addFlashAttribute("msg", "게시물이 정상적으로 삭제되었습니다.");
        return "redirect:/";
    }


}