package com.example.maor.trivia;

import com.example.maor.trivia.Activity.User;

/**
 * Created by orenshadmi on 17/03/2018.
 */

public class WaitingRoom {

    private String waitRoomId;
    private User userWaiting;
    private User userJoining;
    boolean isReady;
    private String gameRoomId;

    public WaitingRoom(User userWaiting,User userJoining, String waitRoomId) {
        this.userWaiting = userWaiting;
        this.waitRoomId  = waitRoomId;
        this.userJoining = userJoining;
        this.isReady = false;
        this.gameRoomId = null;
    }

    public WaitingRoom(){

    }


    public String getGameRoomId() {
        return gameRoomId;
    }

    public void setGameRoomId(String gameRoomId) {
        this.gameRoomId = gameRoomId;
    }

    public User getUserWaiting() {
        return userWaiting;
    }

    public User getUserJoining() {
        return userJoining;
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

    public void setUserJoining(User userJoining) {
        this.userJoining = userJoining;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
