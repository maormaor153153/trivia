package com.example.maor.trivia.Class;

/**
 * Created by Maor on 18/03/2018.
 */

public class UserInformation {

    String email;
    String Total_games_played;
    String Win;
    String Lose;
    String High_Score;

    public UserInformation() {
    }

    public String getEmail() {
        return email;
    }

    public String getTotal_games_played() {
        return Total_games_played;
    }

    public String getWin() {
        return Win;
    }

    public String getLose() {
        return Lose;
    }

    public String getHigh_Score() {
        return High_Score;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTotal_games_played(String total_games_played) {
        Total_games_played = total_games_played;
    }

    public void setWin(String win) {
        Win = win;
    }

    public void setLose(String lose) {
        Lose = lose;
    }

    public void setHigh_Score(String high_Score) {
        High_Score = high_Score;
    }
}
