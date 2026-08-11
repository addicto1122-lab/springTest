package com.example.demo.repository;

import com.example.demo.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {
    private final DataSource ds;

    @Autowired
    public PostRepository(DataSource ds) {
        this.ds = ds;
    }

    public List<Post> showPost(){
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, m.name FROM post p JOIN member m ON m.id = p.member_id ORDER BY p.id DESC";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Post post = new Post();
                post.setContent(rs.getString("content"));
                post.setCreate_at(rs.getTimestamp("create_at"));
                post.setId(rs.getInt("id"));
                post.setMember_id(rs.getInt("member_id"));
                post.setTitle(rs.getString("title"));
                post.setWriter(rs.getString("name"));
                posts.add(post);
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post selectPost(int id){
        String sql = "SELECT p.*, m.name FROM post p JOIN member m ON m.id = p.member_id WHERE p.id = ?";

        try (Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            Post post = new Post();
            if(rs.next()){
                post.setContent(rs.getString("content"));
                post.setCreate_at(rs.getTimestamp("create_at"));
                post.setId(rs.getInt("id"));
                post.setMember_id(rs.getInt("member_id"));
                post.setTitle(rs.getString("title"));
                post.setWriter(rs.getString("name"));
                return post;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertPost(String title, String content, int user_id){
        String sql = "INSERT INTO post(title, content, member_id) VALUES (?,?,?)";

        try (Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, user_id);

            int rs = pstmt.executeUpdate();

            return rs > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePost(int post_id, int user_id){
        String sql = "DELETE FROM post WHERE id = ? AND member_id = ?";

        try (Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setInt(1, post_id);
            pstmt.setInt(2, user_id);

            int rs = pstmt.executeUpdate();

            return rs > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
