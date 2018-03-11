package com.example.maor.trivia;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by orenshadmi on 07/03/2018.
 */

public class Quiz {

    private ArrayList<Question> questions;
    private Context mContext;
    private static final int EASY_SCORE = 5;
    private static final int numOfQuestionsPerGame = 10;
    private int numOfQuestionsAsked;
    int score;
    boolean isGameOver;

    public Quiz(Context context) {
        score = 0;
        numOfQuestionsAsked = 0;
        isGameOver = false;
        questions = new ArrayList<>();
        mContext = context;

    }




    public void readTrueFalseQuestionFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("TrueFalseQuestions.txt"), "UTF-8"));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                mLine.replaceAll("\\s+","");
                String[] parts = mLine.split("@");
                String question = parts[0];
                String answer = parts[1];
                answer.replaceAll("\\s+","");

                TrueFalse trueFalseQuestion = new TrueFalse(question,answer);
                questions.add(trueFalseQuestion);


            }
        } catch (IOException e) {
            Log.e("message: ",e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("message: ",e.getMessage());
                }
            }
        }
    }



    public void readMCQuestionsFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("MC.txt"), "UTF-8"));

            String mLine;
            while ((mLine = reader.readLine()) != null) {

                String[] parts = mLine.split("@");
                String question = parts[0];
                String answer = parts[1];
                String choice1 = parts[2];
                String choice2 = parts[3];
                String choice3 = parts[4];

                answer.replaceAll("\\s+","");

                ArrayList<String> choices;
                choices = addToArrayList(answer, choice1, choice2, choice3);

                MultipleChoice mcQuestion = new MultipleChoice(question,choices,answer);
                questions.add(mcQuestion);

            }
        } catch (IOException e) {
            Log.e("message: ",e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("message: ",e.getMessage());
                }
            }
        }
    }

    private ArrayList<String> addToArrayList(String answer, String choice1, String choice2, String choice3) {
        ArrayList<String> choices = new ArrayList<>();
        choices.add(answer);
        choices.add(choice1);
        choices.add(choice2);
        choices.add(choice3);

        return choices;
    }


    public void  setAllQuestionsToNotAsked(){
        for (Question q:questions) {
            q.setAsked(false);
        }
    }

    public boolean isCorrect(int i , String playerClick){
        Question question = questions.get(i);
        if(question instanceof TrueFalse){
            TrueFalse tf =(TrueFalse) question;
            if(tf.isCorrect(playerClick))
                return true;
            else
                return false;
        }
        else if (question instanceof MultipleChoice){
            MultipleChoice mc = (MultipleChoice)question;
            if(mc.isCorrect(playerClick))
                return true;
            else
                return false;
        }

        return false;


    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        if( numOfQuestionsAsked <= numOfQuestionsPerGame){
            return false;
        }
        return true;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public int getNumOfQuestionsAsked() {
        return numOfQuestionsAsked;
    }

    public static int getNumOfQuestionsPerGame() {
        return numOfQuestionsPerGame;
    }

    public void incrementQuestionsAsked(){
        numOfQuestionsAsked++;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "questionsRepo=" + questions +
                '}';
    }

    public int calculateScore(long timeLeft) {
        score = (int) (EASY_SCORE * timeLeft);
        return score;
    }
}