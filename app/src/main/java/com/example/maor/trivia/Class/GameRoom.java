package com.example.maor.trivia.Class;

/**
 * Created by orenshadmi on 18/03/2018.
 */

public class GameRoom {
    private User player1;
    private User player2;
    String gameRoomId;
    Question questionDisplayed;
    Boolean isPlayer1Turn;

    public GameRoom(User player1, User player2, String gameRoomId) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameRoomId = gameRoomId;
    }

    public GameRoom(){

    }


    public Boolean getPlayer1Turn() {
        return isPlayer1Turn;
    }

    public void setPlayer1Turn(Boolean player1Turn) {
        isPlayer1Turn = player1Turn;
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

    public Question getQuestionDisplayed() {
        return questionDisplayed;
    }

    public void setQuestionDisplayed(Question questionDisplayed) {
        this.questionDisplayed = questionDisplayed;
    }
}
