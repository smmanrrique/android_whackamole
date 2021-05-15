package com.example.whackamole.models;

import java.io.Serializable;

public class User implements Serializable {

    Integer user_id;
    String nickname;
    String email;
    String password;
    String join_date;

    public User() {
    }

    public User(Integer user_id, String nickname, String email, String password, String join_date) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.join_date = join_date;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", join_date='" + join_date + '\'' +
                '}';
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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
