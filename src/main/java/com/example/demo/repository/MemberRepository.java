package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class MemberRepository {


    private final DataSource ds;

    @Autowired
    public MemberRepository(DataSource ds) {
        this.ds = ds;
    }

    public boolean insertMember(Member member) {
        String sql = "INSERT INTO member(name, age, email, password) VALUES(?, ?, ?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getName());
            pstmt.setInt(2, member.getAge());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPassword());

            int rs = pstmt.executeUpdate();

            if(rs > 0)
            {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Member loginMember(String id, String password){
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";

        try (Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, id);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            Member member = new Member();

            if(rs.next()){
                member.setPassword(rs.getString("password"));
                member.setEmail(rs.getString("email"));
                member.setName(rs.getString("name"));
                member.setAge(rs.getInt("age"));
                member.setId(rs.getInt("id"));
                return member;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member selectMember(int id){
        String sql = "SELECT * FROM member WHERE id = ?";

        try (Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            Member member = new Member();

            if (rs.next()){
                member.setPassword(rs.getString("password"));
                member.setEmail(rs.getString("email"));
                member.setName(rs.getString("name"));
                member.setAge(rs.getInt("age"));
                member.setId(rs.getInt("id"));
                return member;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member updateMember(Member member){
        String sql = "UPDATE member SET email = ?, name = ?, age = ?, password = ? WHERE id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getName());
            pstmt.setInt(3, member.getAge());
            pstmt.setString(4, member.getPassword());
            pstmt.setInt(5, member.getId());

            int result = pstmt.executeUpdate();

            if (result > 0) {
                return member;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMember(int id){
        String sql = "DELETE FROM member WHERE id = ?";

        try (Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            int rs = pstmt.executeUpdate();

            return rs > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}