package com.example.demo.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Post {
    private int id;
    private String title;
    private String content;
    private int member_id;
    private Date create_at;
    private int view_count;
}
