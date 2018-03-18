package com.example.maor.trivia.Class;

/**
 * Created by Maor on 17/02/2018.
 */

public class Questions {

    public String mQuestions[]={
             "A Java application can accept any number of arguments from the command line?",
            "Which command is used to find an integer argument when reading from the command line?",
            "Primitive data types are stored by reference in memory?",
            "Which type of loop is considered a post-test loop?",
    };

    private String mChoices[][]= {
            {"path ","java ","location ","jdk"},
            {"Integer.parseInteger(arg[0])","int.parseInteger(arg[0])","parseInteger(arg[0])","int.parseInt(arg[0])"},
            {"word1.equals(word2)","word1 == word2 ","word1 = word2  ","word1 != word2"},
            {"do…while ","for ","while  ","for…while"},

    };
    private String mCorrectAnswers[]={"path ","Integer.parseInteger(arg[0])","word1.equals(word2) ","do…while"};

    public String getQuestion(int a)
    {
        String question = mQuestions[a];
        return question;
    }

    public String getChoice1(int a)
    {
        String choice = mChoices[a][0];
        return choice;
    }
    public String getChoice2(int a)
    {
        String choice = mChoices[a][1];
        return choice;
    }
    public String getChoice3(int a)
    {
        String choice = mChoices[a][2];
        return choice;
    }
    public String getChoice4(int a)
    {
        String choice = mChoices[a][3];
        return choice;
    }

    public String getCorrectAnswer(int a)
    {
        String answer = mCorrectAnswers[a];
        return answer;
    }
}


