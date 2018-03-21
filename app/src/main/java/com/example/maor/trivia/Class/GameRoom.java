package com.example.maor.trivia.Class;

import com.example.maor.trivia.Activity.User;

/**
 * Created by orenshadmi on 18/03/2018.
 */

public class GameRoom {
    private User player1;
    private User player2;
    String gameRoomId;
    int numOfQuestionToDisplay;
    String whoPlay;
    Long optionClicked;
    boolean isTimeStopped;


    public GameRoom(User player1, User player2, String gameRoomId, int numOfQuestion) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameRoomId = gameRoomId;
        this.numOfQuestionToDisplay =numOfQuestion;
        this.whoPlay = null;
        this.optionClicked = Long.valueOf(-1);
        this.isTimeStopped = false;
    }


    public boolean isTimeStopped() {
        return isTimeStopped;
    }

    public void setTimeStopped(boolean timeStopped) {
        isTimeStopped = timeStopped;
    }

    public String getWhoPlay() {
        return whoPlay;
    }

    public Long getOptionClicked() {
        return optionClicked;
    }

    public void setWhoPlay(String whoPlay) {
        this.whoPlay = whoPlay;
    }

    public void setOptionClicked(Long optionClicked) {
        this.optionClicked = optionClicked;
    }



    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public void setGameRoomId(String gameRoomId) {
        this.gameRoomId = gameRoomId;
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public String getGameRoomId() {
        return gameRoomId;
    }

    public int getNumOfQuestionToDisplay() {
        return numOfQuestionToDisplay;
    }

    public void setNumOfQuestionToDisplay(int numOfQuestionToDisplay) {
        this.numOfQuestionToDisplay = numOfQuestionToDisplay;
    }
}
