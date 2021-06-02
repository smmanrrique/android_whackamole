package com.example.whackamole.models;


import java.io.Serializable;

public class User implements Serializable {
    Integer id;
    String nickname;
    String email;
    String password;
    String date_of_joining;

    public User() {
    }

    public User(Integer id, String nickname, String email, String password, String date_of_joining) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.date_of_joining = date_of_joining;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", date_of_joining='" + date_of_joining + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate_of_joining() {
        return date_of_joining;
    }

    public void setDate_of_joining(String date_of_joining) {
        this.date_of_joining = date_of_joining;
    }
}
