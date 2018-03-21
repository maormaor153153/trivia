package com.example.maor.trivia.Activity;

/**
 * Created by orenshadmi on 17/03/2018.
 */



public class User {
    private String name, id;
    private boolean isTurn = false;
    private int score;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        this.score = 0;
    }
    public User(){

    }

    public User(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String pushId) {
        this.id = pushId;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }
}
