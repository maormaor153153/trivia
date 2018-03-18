package com.example.maor.trivia.Class;

/**
 * Created by orenshadmi on 17/03/2018.
 */



public class User {
    private String name, id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public User(){

    }

    public User(String name) {
        this.name = name;
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
}
