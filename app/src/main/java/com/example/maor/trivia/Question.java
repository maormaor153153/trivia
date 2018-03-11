package com.example.maor.trivia;

/**
 * Created by orenshadmi on 07/03/2018.
 */

public class Question {

    private String question;
    private boolean isAsked;


    public Question(String question) {
        this.question = question;
        isAsked = false;
    }

    public Question() {

    }

    public boolean isAsked() {
        return isAsked;
    }

    public void setAsked(boolean asked) {
        isAsked = asked;
    }

    boolean isCorrect(){
        return false;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                '}';
    }


}
