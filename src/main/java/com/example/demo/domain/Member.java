package com.example.demo.domain;

import lombok.Data;

@Data
public class Member {
    private int id;
    private String name;
    private int age;
    private String email;
    private String password;
}
