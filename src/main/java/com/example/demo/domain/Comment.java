package com.example.demo.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private int id;
    private int post_id;
    private int member_id;
    private String content;
    private Date create_at;
    private String name;
}
