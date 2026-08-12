package com.example.demo.repository;

import com.example.demo.domain.Comment;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepository {
    private final DataSource ds;

    public CommentRepository(DataSource ds) {
        this.ds = ds;
    }

    public List<Comment> showComment(int post_id){
        String sql = "SELECT c.*, m.name FROM comment c JOIN member m ON c.member_id = m.id WHERE post_id = ?";
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, post_id);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Comment comment = new Comment();
                comment.setContent(rs.getString("content"));
                comment.setCreate_at(rs.getDate("create_at"));
                comment.setId(rs.getInt("comment_id"));
                comment.setMember_id(rs.getInt("member_id"));
                comment.setName(rs.getString("name"));
                comment.setPost_id(rs.getInt("post_id"));
                comments.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }
}
