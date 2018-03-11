package com.example.maor.trivia;

/**
 * Created by orenshadmi on 07/03/2018.
 */

public class TrueFalse extends Question{


    private String answer;

    public TrueFalse(String question, String answer) {
        super(question);
        this.answer = answer;
    }


    public boolean isCorrect(String str){
        str.replaceAll("\\s+","");
        if( str.toLowerCase().equals(answer.toLowerCase()))
            return true;

        return false;
    }


    @Override
    public String toString() {
        return "TrueFalse{"
                +"Question:"+ getQuestion()+
                "answer='" + answer + '\'' +
                '}';
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
