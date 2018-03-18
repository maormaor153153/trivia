package com.example.maor.trivia.Class;

import java.util.ArrayList;

/**
 * Created by orenshadmi on 07/03/2018.
 */

public class MultipleChoice extends Question {

    private ArrayList<String> mChoices;
    private String mAnswer;


    public MultipleChoice(String question, ArrayList<String> choices, String answer) {
        super(question);
        mAnswer = answer;
        mChoices = choices;

    }

    public void setmChoices(ArrayList<String> mChoices) {
        this.mChoices = mChoices;
    }

    public void setmAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }

    public ArrayList<String> getmChoices() {
        return mChoices;
    }

    public String getmAnswer() {
        return mAnswer;
    }

    @Override
    public String toString() {
        return "MultipleChoice{" +
                "mChoices=" + mChoices +
                ", mAnswer='" + mAnswer + '\'' +
                '}';
    }

    public boolean isCorrect(String str){
        str.replaceAll("\\s+","");
        if( str.equals(mAnswer))
            return true;

        return false;
    }
}
