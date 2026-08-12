package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public List<Post> showPost(){
        return postRepository.showPost();
    }

    public Post selectPost(int id){
        return postRepository.selectPost(id);
    }

    public boolean insertPost(String title, String content, int id){
        return postRepository.insertPost(title, content, id);
    }

    public String deletePost(int post_id, int user_id){
        Post post = postRepository.selectPost(post_id);

        if (post.getMember_id() != user_id){
            return "아이디 불일치";
        }
        boolean c = postRepository.deletePost(post_id, user_id);
        if (c){
            return "성공";
        }else{
            return "실패";
        }
    }

    public boolean updatePost(Post post){
        return postRepository.updatePost(post);
    }

}
