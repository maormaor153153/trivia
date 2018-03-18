package com.example.maor.trivia;

import com.example.maor.trivia.Class.User;

/**
 * Created by orenshadmi on 17/03/2018.
 */

public class WaitingRoom {

    private String waitRoomId;
    private User userWaiting;
    private User userJoinig;
    boolean isReady;

    public WaitingRoom(User userWaiting,User userJoining, String waitRoomId) {
        this.userWaiting = userWaiting;
        this.waitRoomId  = waitRoomId;
        this.userJoinig = userJoining;
        this.isReady = false;
    }

    public WaitingRoom(){

    }

    public User getUserWaiting() {
        return userWaiting;
    }

    public User getUserJoinig() {
        return userJoinig;
    }

    public String getWaitRoomId() {
        return waitRoomId;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setWaitRoomId(String waitRoomId) {
        this.waitRoomId = waitRoomId;
    }

    public void setUserWaiting(User userWaiting) {
        this.userWaiting = userWaiting;
    }

    public void setUserJoinig(User userJoinig) {
        this.userJoinig = userJoinig;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
