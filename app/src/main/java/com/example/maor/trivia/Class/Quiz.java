package com.example.maor.trivia.Class;

import android.content.Context;
import android.util.Log;

import com.example.maor.trivia.TrueFalse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by orenshadmi on 07/03/2018.
 */

public class Quiz {

    private ArrayList<Question> questions;
    private Context mContext;
    private static final int EASY_SCORE = 5;
    private static final int NUM_OF_QUESTIONS_PER_GAME = 10;
    private int numOfQuestionsAsked;
    int score;
    boolean isGameOver, isUserTurn, isOpenentTurn;


    public Quiz(Context context) {
        score = 0;
        isUserTurn = isOpenentTurn = false;
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
//                initMCQuestion(choices);
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

    public void setUserTurn(boolean userTurn) {
        isUserTurn = userTurn;
    }

    public void setOpenentTurn(boolean openentTurn) {
        isOpenentTurn = openentTurn;
    }

    public boolean isUserTurn() {
        return isUserTurn;
    }

    public boolean isOpenentTurn() {
        return isOpenentTurn;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        if( numOfQuestionsAsked <= NUM_OF_QUESTIONS_PER_GAME){
            return false;
        }
        return true;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static int getEasyScore() {
        return EASY_SCORE;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setNumOfQuestionsAsked(int numOfQuestionsAsked) {
        this.numOfQuestionsAsked = numOfQuestionsAsked;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public int getNumOfQuestionsAsked() {
        return numOfQuestionsAsked;
    }

    public static int getNumOfQuestionsPerGame() {
        return NUM_OF_QUESTIONS_PER_GAME;
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

    private void initMCQuestion(ArrayList<String> options) {



        Random r = new Random();
        int i = 0;
        String[] arr = {null, null, null, null};
        int []arrTocheck = {0,0,0,0};


        //Random place for the answers
        do {
            int index = r.nextInt(options.size());
            if(arrTocheck[index] == 0){
                arrTocheck[index] = 1;
                String choice = options.get(index);
                arr[i] = choice;
                i++;
            }
        } while (!isAllPlaced(arrTocheck));


        setOptionToArrayList(options, arr);


    }

    private void setOptionToArrayList(ArrayList<String> options, String[] arr) {
        options.set(0,arr[0]);
        options.set(1,arr[1]);
        options.set(2,arr[2]);
        options.set(3,arr[3]);
    }


    private boolean isAllPlaced(int[] arr) {
        for(int i = 0 ; i < arr.length ; i++){
            if(arr[i] == 0)
                return false;
        }
        return true;
    }
}