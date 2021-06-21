package com.example.whackamole.models;

import java.io.Serializable;

public class Game implements Serializable {

    public Integer id;
    public String guest;
    public Integer user_id;
    public String winner;
    public Integer score;
    public Integer status;
    public String created_date;

    public Game(Integer user_id, String guest,  Integer status) {
        this.guest = guest;
        this.user_id = user_id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", guest='" + guest + '\'' +
                ", user_id=" + user_id +
                ", winner='" + winner + '\'' +
                ", score=" + score +
                ", status=" + status +
                ", created_date='" + created_date + '\'' +
                '}';
    }
}
