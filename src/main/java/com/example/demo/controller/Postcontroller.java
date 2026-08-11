package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class Postcontroller {

    private final PostService postService;


    @GetMapping("/detail")
    public String getPostDetail(@RequestParam("id") int id, Model model){
        Post post = postService.selectPost(id);
        model.addAttribute("post", post);
        return "post/detail";
    }
}
