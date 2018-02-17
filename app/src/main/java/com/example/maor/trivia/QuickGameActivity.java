package com.example.maor.trivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class QuickGameActivity extends AppCompatActivity {


    Button answer1,answer2,answer3,answer4;
    TextView questions ,score;


    private  Questions mQuestions = new Questions();
    private String mAnswer;
    private int mScore =0;
    private int mQuestionLength =  mQuestions.mQuestions.length;

    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game);

        r = new Random();

        answer1= (Button) findViewById(R.id.answer1);
        answer2= (Button) findViewById(R.id.answer2);
        answer3= (Button) findViewById(R.id.answer3);
        answer4= (Button) findViewById(R.id.answer4);

        questions= (TextView) findViewById(R.id.question);
        score = (TextView) findViewById(R.id.score);
        score.setText("Score: "+ mScore);

        updateQuestion(r.nextInt(mQuestionLength));

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer1.getText() == mAnswer){
                    mScore++;
                    score.setText("Score: "+ mScore);
                    updateQuestion(r.nextInt(mQuestionLength));
                }
                else
                {
                    gameOver();
                }
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer2.getText() == mAnswer){
                    mScore++;
                    score.setText("Score: "+ mScore);
                    updateQuestion(r.nextInt(mQuestionLength));
                }
                else
                {
                    gameOver();
                }
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer3.getText() == mAnswer){
                    mScore++;
                    score.setText("Score: "+ mScore);
                    updateQuestion(r.nextInt(mQuestionLength));
                }
                else
                {
                    gameOver();
                }
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer4.getText() == mAnswer){
                    mScore++;
                    score.setText("Score: "+ mScore);
                    updateQuestion(r.nextInt(mQuestionLength));
                }
                else
                {
                    gameOver();
                }
            }
        });


    }

    private void updateQuestion(int num) {
        questions.setText(mQuestions.getQuestion(num));
        answer1.setText(mQuestions.getChoice1(num));
        answer2.setText(mQuestions.getChoice2(num));
        answer3.setText(mQuestions.getChoice3(num));
        answer4.setText(mQuestions.getChoice4(num));

        mAnswer = mQuestions.getCorrectAnswer(num);

    }

    private void gameOver()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuickGameActivity.this);
        alertDialogBuilder
                .setMessage("Game Over ! your score is "+ mScore+ " points")
                .setCancelable(false)
                .setPositiveButton("New Game",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(getApplicationContext(),QuickGameActivity.class));
                    }
                })
               .setNegativeButton("Exit",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                        finish();
                }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
